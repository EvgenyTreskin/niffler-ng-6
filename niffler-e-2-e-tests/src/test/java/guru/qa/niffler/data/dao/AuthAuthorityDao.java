package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;

import java.util.Optional;
import java.util.UUID;

public interface AuthAuthorityDao {

    AuthAuthorityEntity create(AuthAuthorityEntity authAuthority);

    Optional<AuthAuthorityEntity> findById(UUID id);

    Optional<AuthAuthorityEntity> findByUserId(UUID userId);

    void delete(AuthAuthorityEntity authAuthority);
}
