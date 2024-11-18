package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import io.qameta.allure.Step;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class SpendApiClient {

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().spendUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @Step("Создание новой траты")
    public @Nullable SpendJson createSpend(SpendJson spend) {
        try {
            Response<SpendJson> response = spendApi.addSpend(spend).execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                throw new RuntimeException("Failed to create spend: " + response.errorBody().string());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while creating spend", e);
        }
    }

    @Step("Редактирование траты")
    public @Nullable SpendJson editSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.editSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Получение {count} траты: {username}")
    public @Nullable SpendJson getSpend(String id, String username) {
        final Response<SpendJson> response;
        try {
            response = spendApi.getSpend(id, username)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Получение всех трат пользователя: {username}")
    public @Nonnull List<SpendJson> allSpends(String username,
                                              @Nullable CurrencyValues currency,
                                              @Nullable String from,
                                              @Nullable String to) {
        final Response<List<SpendJson>> response;
        try {
            response = spendApi.getAllSpends(username, currency, from, to)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body() != null
                ? response.body()
                : Collections.emptyList();
    }

    @Step("Удаление {count} траты: {username}")
    public void removeSpend(String username, String... ids) {
        final Response<Void> response;
        try {
            response = spendApi.removeSpend(username, Arrays.stream(ids).toList())
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }

    @Step("Создание новой категории")
    public CategoryJson createCategory(CategoryJson category) {
        try {
            Response<CategoryJson> response = spendApi.addCategory(category).execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                throw new RuntimeException("Failed to create category: " + response.errorBody().string());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while creating category", e);
        }
    }

    @Step("Обновление категории")
    public @Nullable CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.updateCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Получение всех категорий пользователя: {username}")
    public @Nonnull List<CategoryJson> getAllCategories(String username, boolean excludeArchived) {
        final Response<List<CategoryJson>> response;
        try {
            response = spendApi.getCategories(username, excludeArchived)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body() != null
                ? response.body()
                : Collections.emptyList();
    }

    @Step("Удаление категории")
    public void removeCategory(CategoryJson category) {
        throw new UnsupportedOperationException("Deleting a category is not supported by API");
    }
}
