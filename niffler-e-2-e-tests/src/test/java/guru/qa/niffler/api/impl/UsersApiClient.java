package guru.qa.niffler.api.impl;

import guru.qa.niffler.api.UserApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import io.qameta.allure.Step;
import retrofit2.Response;
import wiremock.com.google.common.base.Stopwatch;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@ParametersAreNonnullByDefault
public class UsersApiClient extends RestClient implements UsersClient {

    private final AuthApiClient authApiClient = new AuthApiClient();
    private final UserApi userApi;

    public UsersApiClient() {
        super(CFG.userdataUrl());
        this.userApi = retrofit.create(UserApi.class);
    }

    @Nonnull
    @Override
    @Step("Создание пользователя: {username}")
    public UserJson createUser(String username, String password) throws IOException {
        // Запрос формы регистрации для получения CSRF токена
        authApiClient.requestRegisterForm();
        authApiClient.registerUser(
                username,
                password,
                password,
                ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
        );

        // Ожидание появления пользователя после регистрации
        long maxWaitTime = 5000L; // 5 секунд ожидания
        Stopwatch sw = Stopwatch.createStarted();

        while (sw.elapsed(TimeUnit.MILLISECONDS) < maxWaitTime) {
            try {
                UserJson userJson = userApi.getCurrentUser(username).execute().body();
                if (userJson != null && userJson.id() != null) {
                    return userJson; // Пользователь найден, возвращаем
                } else {
                    Thread.sleep(100); // Ожидание перед следующей проверкой
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Ошибка при выполнении запроса на получение пользователя или ожидании", e);
            }
        }
        // Если пользователь не найден за отведенное время
        throw new AssertionError("Пользователь не был найден в системе после истечения времени ожидания");
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

    @Nonnull
    @Override
    @Step("Добавление в БД и возвращения списка  {count} входящих приглашений пользователя: {targetUser.username}")
    public List<String> addIncomeInvitationList(UserJson targetUser, int count) throws IOException {
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

    @Nonnull
    @Override
    @Step("Добавление в БД и возвращения списка {count} исходящих приглашений пользователя: {targetUser.username}")
    public List<String> addOutcomeInvitationList(UserJson targetUser, int count) throws IOException {
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

    @Nullable
    @Step("Получение текущего пользователя: {username}")
    public UserJson getCurrentUser(String username) throws IOException {
        Response<UserJson> response = userApi.getCurrentUser(username).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Failed to fetch current user.");
        }
    }

    @Nullable
    @Step("Обновление текущего пользователя: {user.username}")
    public UserJson updateUser(UserJson user) throws IOException {
        Response<UserJson> response = userApi.updateUser(user).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Failed to update user.");
        }
    }

    @Nonnull
    @Step("Получение пользователя с именем: {username}")
    public List<UserJson> getAllUsers(String username, String searchQuery) throws IOException {
        Response<List<UserJson>> response = userApi.getAllUsers(username, searchQuery).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Failed to fetch all users.");
        }
    }

    @Nonnull
    @Step("Добавление статуса друзей пользователям: {username}")
    public List<UserJson> getFriends(String username, String searchQuery) throws IOException {
        Response<List<UserJson>> response = userApi.getFriends(username, searchQuery).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Failed to fetch friends.");
        }
    }

    @Nullable
    @Step("Отправка приглашения от пользователя пользователю: {username}")
    public UserJson sendInvitation(String username, String targetUsername) throws IOException {
        Response<UserJson> response = userApi.sendInvitation(username, targetUsername).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body();
        } else {
            throw new IOException("Failed to send invitation.");
        }
    }

    @Nullable
    @Step("Отмена приглашения пользователя: {username} от пользователя: {targetUsername}")
    public UserJson declineInvitation(String username, String targetUsername) throws IOException {
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
