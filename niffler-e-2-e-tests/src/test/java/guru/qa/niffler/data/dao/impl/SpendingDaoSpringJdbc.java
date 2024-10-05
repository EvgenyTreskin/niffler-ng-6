package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendingDaoSpringJdbc implements SpendDao {
    @Override
    public SpendEntity create(SpendEntity spend) {
        return null;
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<SpendEntity> findAllSpendsByUsername(String username) {
        return List.of();
    }

    @Override
    public void deleteSpend(SpendEntity spend) {

    }
}
