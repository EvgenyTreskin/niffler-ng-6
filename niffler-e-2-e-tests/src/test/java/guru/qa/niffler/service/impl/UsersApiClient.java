package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.UserApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import io.qameta.allure.Step;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class UsersApiClient implements UsersClient {

    private final Retrofit userRetrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().userdataUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();
    private final UserApi userApi = userRetrofit.create(UserApi.class);

    private final Retrofit authRetrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().authUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();
    private final AuthApi authApi = authRetrofit.create(AuthApi.class);

    @Override
    @Step("Создание пользователя: {username}")
    public @Nonnull UserJson createUser(String username, String password) throws IOException {
        // Шаг 1: Запрос формы регистрации для получения CSRF токена
        final Response<Void> formResponse;
        try {
            formResponse = authApi.requestRegisterForm().execute();
        } catch (IOException e) {
            throw new AssertionError("Ошибка при выполнении запроса формы регистрации", e);
        }

        // Убедиться, что запрос формы выполнен успешно
        assertEquals(200, formResponse.code(), "Ожидался код 200 при запросе формы регистрации");

        // Шаг 2: Извлечение CSRF токена из заголовка ответа
        String cookieHeader = formResponse.headers().get("Set-Cookie");
        if (cookieHeader == null || !cookieHeader.contains("XSRF-TOKEN")) {
            throw new AssertionError("Не удалось получить XSRF-TOKEN из заголовка Set-Cookie");
        }

        String csrfToken = null;
        for (String cookie : cookieHeader.split(";")) {
            if (cookie.contains("XSRF-TOKEN")) {
                csrfToken = cookie.split("=")[1].trim();
                break;
            }
        }

        if (csrfToken == null) {
            throw new AssertionError("XSRF-TOKEN не найден в заголовке Set-Cookie");
        }

        // Шаг 3: Отправка запроса на регистрацию пользователя
        final Response<Void> registerResponse;
        try {
            registerResponse = authApi.register(username, password, password, csrfToken).execute();
        } catch (IOException e) {
            throw new AssertionError("Ошибка при выполнении запроса регистрации пользователя", e);
        }

        assertEquals(201, registerResponse.code(), "Ожидался код 201 для успешной регистрации");

        // Шаг 4: Получение информации о созданном пользователе
        return Objects.requireNonNull(getCurrentUser(username), "Пользователь не был найден после регистрации");
    }

    @Override
    @Step("Добавление {count} входящих приглашений пользователю: {targetUser.username} в БД")
    public void addIncomeInvitation(UserJson targetUser, int count) throws IOException {
        if (count > 0) {
            // Шаг 1: Проверка, существует ли целевой пользователь (targetUser)
            UserJson user = getCurrentUser(targetUser.username());

            if (user == null || user.id() == null) {
                throw new AssertionError("Пользователь с именем " + targetUser.username() + " не найден");
            }

            for (int i = 0; i < count; i++) {
                // Шаг 2: Создание рандомного пользователя
                UserJson newUser = createUser(randomUsername(), "12345");

                // Шаг 3: Отправка приглашения в друзья
                sendInvitation(newUser.username(), user.username());
            }
        }
    }

    @Override
    @Step("Добавление {count} исходящих приглашений пользователю: {targetUser.username} в БД")
    public void addOutcomeInvitation(UserJson targetUser, int count) throws IOException {
        if (count > 0) {
            // Шаг 1: Проверка, существует ли целевой пользователь (targetUser)
            UserJson user = getCurrentUser(targetUser.username());

            if (user == null || user.id() == null) {
                throw new AssertionError("Пользователь с именем " + targetUser.username() + " не найден");
            }

            for (int i = 0; i < count; i++) {
                // Шаг 2: Создание рандомного пользователя
                UserJson newUser = createUser(randomUsername(), "12345");

                // Шаг 3: Отправка приглашения в друзья
                sendInvitation(user.username(), newUser.username());
            }
        }
    }

    @Override
    @Step("Добавление в БД и возвращения списка  {count} входящих приглашений пользователя: {targetUser.username}")
    public @Nonnull List<String> addIncomeInvitationList(UserJson targetUser, int count) throws IOException {
        List<String> incomeUsers = new ArrayList<>();

        if (count > 0) {
            // Шаг 1: Проверка, существует ли целевой пользователь (targetUser)
            UserJson user = getCurrentUser(targetUser.username());

            if (user == null || user.id() == null) {
                throw new AssertionError("Пользователь с именем " + targetUser.username() + " не найден");
            }

            for (int i = 0; i < count; i++) {
                // Шаг 2: Создание рандомного пользователя
                UserJson newUser = createUser(randomUsername(), "12345");

                // Шаг 3: Отправка приглашения в друзья
                sendInvitation(newUser.username(), user.username());

                incomeUsers.add(newUser.username());
            }
        }
        return incomeUsers;
    }

    @Override
    @Step("Добавление в БД и возвращения списка {count} исходящих приглашений пользователя: {targetUser.username}")
    public @Nonnull List<String> addOutcomeInvitationList(UserJson targetUser, int count) throws IOException {
        List<String> outcomeUsers = new ArrayList<>();

        if (count > 0) {
            // Шаг 1: Проверка, существует ли целевой пользователь (targetUser)
            UserJson user = getCurrentUser(targetUser.username());

            if (user == null || user.id() == null) {
                throw new AssertionError("Пользователь с именем " + targetUser.username() + " не найден");
            }

            for (int i = 0; i < count; i++) {
                // Шаг 2: Создание рандомного пользователя
                UserJson newUser = createUser(randomUsername(), "12345");

                // Шаг 3: Отправка приглашения в друзья
                sendInvitation(user.username(), newUser.username());

                // Добавляем созданного пользователя в список
                outcomeUsers.add(newUser.username());
            }
        }
        return outcomeUsers;
    }

    @Override
    @Step("Добавление {count} друзей пользователю: {targetUser.username} в БД")
    public void addFriend(UserJson targetUser, int count) throws IOException {
        if (count > 0) {
            // Шаг 1: Проверка, существует ли целевой пользователь (targetUser)
            UserJson user = getCurrentUser(targetUser.username());

            if (user == null || user.id() == null) {
                throw new AssertionError("Пользователь с именем " + targetUser.username() + " не найден");
            }

            for (int i = 0; i < count; i++) {
                // Шаг 2: Отправка входящего приглашения в друзья
                List<String> incomeUsers = addIncomeInvitationList(targetUser, 1);

                // Шаг 3: Принятие входящего приглашения в друзья
                userApi.acceptInvitation(user.username(), incomeUsers.get(0));
            }
        }
    }

    @Nonnull
    @Override
    @Step("Добавление в БД и возвращение списка {count} друзей пользователя: {targetUser.username}")
    public List<String> addFriendList(UserJson targetUser, int count) throws IOException {
        List<String> friends = new ArrayList<>();

        if (count > 0) {
            // Шаг 1: Проверка, существует ли целевой пользователь (targetUser)
            UserJson user = getCurrentUser(targetUser.username());

            if (user == null || user.id() == null) {
                throw new AssertionError("Пользователь с именем " + targetUser.username() + " не найден");
            }

            for (int i = 0; i < count; i++) {
                // Шаг 2: Отправка входящего приглашения в друзья
                List<String> incomeUsers = addIncomeInvitationList(targetUser, 1);

                // Шаг 3: Принятие входящего приглашения в друзья
                userApi.acceptInvitation(user.username(), incomeUsers.get(0));

                // Добавляем созданного друга в список
                friends.add(incomeUsers.get(0));
            }
        }
        return friends;
    }

    @Step("Получение текущего пользователя: {username}")
    public @Nullable UserJson getCurrentUser(String username) throws IOException {
        Response<UserJson> response = userApi.getCurrentUser(username).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Failed to fetch current user.");
        }
    }

    @Step("Обновление текущего пользователя: {user.username}")
    public @Nullable UserJson updateUser(UserJson user) throws IOException {
        Response<UserJson> response = userApi.updateUser(user).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Failed to update user.");
        }
    }

    @Step("Получение пользователя с именем: {username}")
    public @Nonnull List<UserJson> getAllUsers(String username, String searchQuery) throws IOException {
        Response<List<UserJson>> response = userApi.getAllUsers(username, searchQuery).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Failed to fetch all users.");
        }
    }

    @Step("Добавление статуса друзей пользователям: {username}")
    public @Nonnull List<UserJson> getFriends(String username, String searchQuery) throws IOException {
        Response<List<UserJson>> response = userApi.getFriends(username, searchQuery).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Failed to fetch friends.");
        }
    }

    @Step("Отправка приглашения от пользователя пользователю: {username}")
    public @Nullable UserJson sendInvitation(String username, String targetUsername) throws IOException {
        Response<UserJson> response = userApi.sendInvitation(username, targetUsername).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Failed to send invitation.");
        }
    }

    @Step("Отмена приглашения пользователя: {username} от пользователя: {targetUsername}")
    public @Nullable UserJson declineInvitation(String username, String targetUsername) throws IOException {
        Response<UserJson> response = userApi.declineInvitation(username, targetUsername).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Failed to decline invitation.");
        }
    }

    @Step("Удаление приглашения пользователя: {username} от пользователя: {targetUsername}")
    public void removeFriend(String username, String targetUsername) throws IOException {
        Response<Void> response = userApi.removeFriend(username, targetUsername).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Failed to remove friend.");
        }
    }
}
