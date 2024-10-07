package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthAuthorityDao {
    void create(AuthorityEntity... authAuthority);

    Optional<AuthorityEntity> findById(UUID id);

    Optional<AuthorityEntity> findByUserId(UUID userId);

    void delete(AuthorityEntity authAuthority);

    List<AuthorityEntity> findAll();
}
