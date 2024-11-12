package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.SpendRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.SpendRepositorySpringJdbc;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;

import java.util.Optional;
import java.util.UUID;

public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();

    private final SpendRepository spendRepository = new SpendRepositoryJdbc();
    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public SpendJson createSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> SpendJson.fromEntity(
                        spendRepository.create(SpendEntity.fromJson(spend))
                )
        );
    }

    @Override
    public SpendJson updateSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(() ->
                SpendJson.fromEntity(spendRepository.update(SpendEntity.fromJson(spend))));
    }

    @Override
    public Optional<SpendJson> findSpendById(UUID id) {
        return xaTransactionTemplate.execute(() -> {
            Optional<SpendEntity> optionalSpendEntity = spendRepository.findById(id);
            return optionalSpendEntity.map(SpendJson::fromEntity);
        });
    }

    @Override
    public Optional<SpendJson> findSpendByUsernameAndDescription(String username, String description) {
        return xaTransactionTemplate.execute(() -> {
            Optional<SpendEntity> spendEntities = spendRepository.findByUsernameAndSpendDescription(username, description);
            return spendEntities.map(SpendJson::fromEntity);
        });
    }

    @Override
    public void removeSpend(SpendJson spend) {
        xaTransactionTemplate.execute(() -> {
            spendRepository.remove(SpendEntity.fromJson(spend));
            return null;
        });
    }

    public CategoryJson createCategory(CategoryJson category) {
        return xaTransactionTemplate.execute(() -> CategoryJson.fromEntity(
                        spendRepository.createCategory(CategoryEntity.fromJson(category))
                )
        );
    }

    @Override
    public CategoryJson updateCategory(CategoryJson category) {
        return xaTransactionTemplate.execute(() ->
                CategoryJson.fromEntity(spendRepository.updateCategory(CategoryEntity.fromJson(category))));
    }

    @Override
    public Optional<CategoryJson> findCategoryById(UUID id) {
        return xaTransactionTemplate.execute(() -> {
            Optional<CategoryEntity> optionalCategoryEntity = spendRepository.findCategoryById(id);
            return optionalCategoryEntity.map(CategoryJson::fromEntity);
        });
    }

    @Override
    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String name) {
        return xaTransactionTemplate.execute(() -> {
            Optional<CategoryEntity> optionalCategoryEntity = spendRepository.findCategoryByUsernameAndCategoryName(username, name);
            return optionalCategoryEntity.map(CategoryJson::fromEntity);
        });
    }

    public void removeCategory(CategoryJson category) {
        xaTransactionTemplate.execute(() -> {
                    spendRepository.removeCategory(CategoryEntity.fromJson(category));
                    return null;
                }
        );
    }
}
