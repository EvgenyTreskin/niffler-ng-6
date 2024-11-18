package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositoryJdbc;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();

    private final SpendRepository spendRepository = new SpendRepositoryJdbc();
    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    @Nonnull
    @Override
    @Step("Создание новой траты")
    public SpendJson createSpend(SpendJson spend) {
        return Objects.requireNonNull(
                xaTransactionTemplate.execute(() ->
                        SpendJson.fromEntity(spendRepository.create(SpendEntity.fromJson(spend))
                )
        ));
    }

    @Nonnull
    @Override
    @Step("Изменение траты")
    public SpendJson updateSpend(SpendJson spend) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() ->
                SpendJson.fromEntity(spendRepository.update(SpendEntity.fromJson(spend)))));
    }

    @Nullable
    @Override
    @Step("Поиск траты по id")
    public Optional<SpendJson> findSpendById(UUID id) {
        return xaTransactionTemplate.execute(() -> {
            Optional<SpendEntity> optionalSpendEntity = spendRepository.findById(id);
            return optionalSpendEntity.map(SpendJson::fromEntity);
        });
    }

    @Nullable
    @Override
    @Step("Поиск траты по имени: {username} и описанию: {description}")
    public Optional<SpendJson> findSpendByUsernameAndDescription(String username, String description) {
        return xaTransactionTemplate.execute(() -> {
            Optional<SpendEntity> spendEntities = spendRepository.findByUsernameAndSpendDescription(username, description);
            return spendEntities.map(SpendJson::fromEntity);
        });
    }


    @Override
    @Step("Удаление траты")
    public void removeSpend(SpendJson spend) {
        xaTransactionTemplate.execute(() -> {
            spendRepository.remove(SpendEntity.fromJson(spend));
            return null;
        });
    }

    @Nonnull
    @Override
    @Step("Создание новой категории")
    public CategoryJson createCategory(CategoryJson category) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> CategoryJson.fromEntity(
                        spendRepository.createCategory(CategoryEntity.fromJson(category))
                )
        ));
    }

    @Nonnull
    @Override
    @Step("Изменение категории")
    public CategoryJson updateCategory(CategoryJson category) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() ->
                CategoryJson.fromEntity(spendRepository.updateCategory(CategoryEntity.fromJson(category)))));
    }

    @Nullable
    @Override
    @Step("Поиск категории по id")
    public Optional<CategoryJson> findCategoryById(UUID id) {
        return xaTransactionTemplate.execute(() -> {
            Optional<CategoryEntity> optionalCategoryEntity = spendRepository.findCategoryById(id);
            return optionalCategoryEntity.map(CategoryJson::fromEntity);
        });
    }

    @Nullable
    @Override
    @Step("Поиск категории по имени : {username} и названию: {name}")
    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String name) {
        return xaTransactionTemplate.execute(() -> {
            Optional<CategoryEntity> optionalCategoryEntity = spendRepository.findCategoryByUsernameAndCategoryName(username, name);
            return optionalCategoryEntity.map(CategoryJson::fromEntity);
        });
    }

    @Override
    @Step("Удаление категории")
    public void removeCategory(CategoryJson category) {
        xaTransactionTemplate.execute(() -> {
                    spendRepository.removeCategory(CategoryEntity.fromJson(category));
                    return null;
                }
        );
    }
}
