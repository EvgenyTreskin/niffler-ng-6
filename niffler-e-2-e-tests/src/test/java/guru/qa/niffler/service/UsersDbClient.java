package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.dao.impl.UdUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UdUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.utils.RandomDataUtils;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static guru.qa.niffler.utils.RandomDataUtils.randomUserName;

public class UsersDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final UdUserDao udUserDaoSpring = new UdUserDaoSpringJdbc();
    private final UdUserDao udUserDao = new UdUserDaoJdbc();
    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
    private final UserdataUserRepository udUserRepositoryHibernate = new UserdataUserRepositoryHibernate();
    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    public UserJson createUser(String username, String password) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntity(username, password);

                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            udUserRepositoryHibernate.create(userEntity(username)),
                            null
                    );
                }
        );
    }

    public void addIncomeInvitation(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = udUserRepositoryHibernate.findById(targetUser.id()
            ).orElseThrow();
            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            String username = randomUserName();
                            AuthUserEntity authUser = authUserEntity(username, "12345");
                            authUserRepository.create(authUser);
                            UserEntity addressee = udUserRepositoryHibernate.create(userEntity(username));
                            udUserRepositoryHibernate.addIncomeInvitation(targetEntity, addressee);
                            return null;
                        }
                );
            }
        }
    }

    public void addOutcomeInvitation(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = udUserRepositoryHibernate.findById(targetUser.id()
            ).orElseThrow();
            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            String username = randomUserName();
                            AuthUserEntity authUser = authUserEntity(username, "12345");
                            authUserRepository.create(authUser);
                            UserEntity addressee = udUserRepositoryHibernate.create(userEntity(username));
                            udUserRepositoryHibernate.addOutcomeInvitation(targetEntity, addressee);
                            return null;
                        }
                );
            }
        }
    }

    void addFriend(UserJson targetUser, int count) {

    }

    private UserEntity userEntity(String username) {
        UserEntity ue = new UserEntity();
        ue.setUsername(username);
        ue.setCurrency(CurrencyValues.RUB);
        return ue;
    }

    private AuthUserEntity authUserEntity(String username, String password) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(pe.encode(password));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUser(authUser);
                            ae.setAuthority(e);
                            return ae;
                        }
                ).toList()
        );
        return authUser;
    }

    private AuthUserEntity authUserEntityJson(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUser(authUser);
                            ae.setAuthority(e);
                            return ae;
                        }
                ).toList()
        );
        return authUser;
    }

    public UserJson createUserSpringJdbcWithTransaction(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntityJson(user);

                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            udUserDaoSpring.create(UserEntity.fromJson(user)),
                            null
                    );
                }
        );
    }

    public UserJson createUserSpringJdbcWithoutTransaction(UserJson user) {
        AuthUserEntity authUser = authUserEntityJson(user);

        authUserRepository.create(authUser);
        return UserJson.fromEntity(
                udUserDaoSpring.create(UserEntity.fromJson(user)),
                null
        );
    }

    public UserJson createUserJdbcWithTransaction(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntityJson(user);
                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            udUserDao.create(UserEntity.fromJson(user)),
                            null
                    );
                }
        );
    }

    public UserJson createUserJdbcWithoutTransaction(UserJson user) {
        AuthUserEntity authUser = authUserEntityJson(user);
        authUserRepository.create(authUser);
        return UserJson.fromEntity(
                udUserDao.create(UserEntity.fromJson(user)),
                null
        );
    }

    public void addFriend(UserJson requester, UserJson addressee) {
        udUserRepositoryHibernate.addFriend(UserEntity.fromJson(requester), UserEntity.fromJson(addressee));
    }

    public void addInvitation(UserJson requester, UserJson addressee) {
        udUserRepositoryHibernate.addInvitation(UserEntity.fromJson(requester), UserEntity.fromJson(addressee));
    }
}
