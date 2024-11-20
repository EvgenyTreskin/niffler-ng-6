package guru.qa.niffler.api.impl;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import io.qameta.allure.Step;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.*;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class SpendApiClient extends RestClient implements SpendClient {

    private final SpendApi spendApi;

    public SpendApiClient() {
        super(CFG.spendUrl());
        this.spendApi = retrofit.create(SpendApi.class);
    }

    @Nonnull
    @Step("Создание новой траты")
    public SpendJson createSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.addSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(201, response.code(), "Ожидался код 201 для создания траты");
        return requireNonNull(response.body(), "Ответ API вернул null при создании траты");
    }

    @Override
    public SpendJson updateSpend(SpendJson spend) {
        return null;
    }

    @Override
    public Optional<SpendJson> findSpendById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<SpendJson> findSpendByUsernameAndDescription(String username, String description) {
        return Optional.empty();
    }

    @Override
    public void removeSpend(SpendJson spend) {

    }

    @Nullable
    @Step("Редактирование траты")
    public SpendJson editSpend(SpendJson spend) {
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

    @Nullable
    @Step("Получение {count} траты: {username}")
    public SpendJson getSpend(String id, String username) {
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

    @Nonnull
    @Step("Получение всех трат пользователя: {username}")
    public List<SpendJson> allSpends(String username,
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

    @Nonnull
    @Step("Создание новой категории")
    public CategoryJson createCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.addCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code(), "Ожидался код 200 для создания категории");
        return requireNonNull(response.body(), "Ответ API вернул null при создании категории");
    }

    @Nullable
    @Step("Обновление категории")
    public CategoryJson updateCategory(CategoryJson category) {
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

    @Override
    public Optional<CategoryJson> findCategoryById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String name) {
        return Optional.empty();
    }

    @Nonnull
    @Step("Получение всех категорий пользователя: {username}")
    public List<CategoryJson> getAllCategories(String username, boolean excludeArchived) {
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
