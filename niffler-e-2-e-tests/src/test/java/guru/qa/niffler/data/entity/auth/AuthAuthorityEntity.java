package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.data.entity.spend.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class AuthAuthorityEntity implements Serializable {
    private UUID id;
    private Authority authority;
    private UserEntity user;
}
