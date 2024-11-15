package guru.qa.niffler.model;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record AuthUserJson(
        UUID id,
        String username,
        String password,
        Boolean enabled,
        Boolean accountNonExpired,
        Boolean accountNonLocked,
        Boolean credentialsNonExpired,
        List<AuthorityEntity> authorities
) {

    public static AuthUserJson fromEntity(AuthUserEntity entity) {
        return new AuthUserJson(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getEnabled(),
                entity.getAccountNonExpired(),
                entity.getAccountNonLocked(),
                entity.getCredentialsNonExpired(),
                new ArrayList<>(entity.getAuthorities())
        );
    }
}