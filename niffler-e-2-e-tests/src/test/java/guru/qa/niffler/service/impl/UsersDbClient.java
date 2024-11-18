package guru.qa.niffler.service.impl;

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
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryJdbc;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import io.qameta.allure.Step;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@ParametersAreNonnullByDefault
public class UsersDbClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final UdUserDao udUserDaoSpring = new UdUserDaoSpringJdbc();
    private final UdUserDao udUserDao = new UdUserDaoJdbc();
    private final AuthUserRepository authUserRepository = new AuthUserRepositoryJdbc();
    private final UserdataUserRepository userdataUserRepositoryHibernate = new UserdataUserRepositoryJdbc();
    private final UserdataUserRepository userdataUserRepositoryJdbc = new UserdataUserRepositoryJdbc();
    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    @Nonnull
    @Override
    @Step("Создание нового пользователя: {username}")
    public UserJson createUser(String username, String password) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> UserJson.fromEntity(
                        createNewUser(username, password),
                        null
                )
        ));
    }

    @Override
    @Step("Добавление {count} входящих приглашений пользователю: {targetUser.username} в БД")
    public void addIncomeInvitation(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepositoryHibernate.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            String username = randomUsername();
                            userdataUserRepositoryHibernate.sendInvitation(
                                    createNewUser(username, "12345"),
                                    targetEntity
                            );
                            return null;
                        }
                );
            }
        }
    }


    @Nonnull
    @Override
    @Step("Добавление в БД и возвращения списка  {count} входящих приглашений пользователя: {targetUser.username}")
    public List<String> addIncomeInvitationList(UserJson targetUser, int count) {
        List<String> incomes = new ArrayList<>();
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepositoryJdbc.findById(targetUser.id()).orElseThrow();
            for (int i = 0; i < count; i++) {
                String username = randomUsername();
                xaTransactionTemplate.execute(() -> {
                    userdataUserRepositoryJdbc.addFriend(createNewUser(username, "12345"), targetEntity);
                    return null;
                });
                incomes.add(username);  // Сохраняем имя пользователя, который отправил входящее приглашение
            }
        }
        return incomes;
    }

    @Override
    @Step("Добавление {count} исходящих приглашений пользователю: {targetUser.username} в БД")
    public void addOutcomeInvitation(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepositoryHibernate.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            String username = randomUsername();
                            userdataUserRepositoryHibernate.sendInvitation(
                                    targetEntity,
                                    createNewUser(username, "12345")
                            );
                            return null;
                        }
                );
            }
        }
    }

    @Override
    @Step("Добавление в БД и возвращения списка {count} исходящих приглашений пользователя: {targetUser.username}")
    public List<String> addOutcomeInvitationList(UserJson targetUser, int count) {
        List<String> outcomes = new ArrayList<>();
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepositoryJdbc.findById(targetUser.id()).orElseThrow();
            for (int i = 0; i < count; i++) {
                String username = randomUsername();
                xaTransactionTemplate.execute(() -> {
                    userdataUserRepositoryJdbc.addFriend(targetEntity, createNewUser(username, "12345"));
                    return null;
                });
                outcomes.add(username);  // Сохраняем имя пользователя, которому отправили исходящее приглашение
            }
        }
        return outcomes;
    }

    @Override
    @Step("Добавление {count} друзей пользователю: {targetUser.username}")
    public void addFriend(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepositoryHibernate.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            String username = randomUsername();
                            userdataUserRepositoryHibernate.addFriend(
                                    targetEntity,
                                    createNewUser(username, "12345")
                            );
                            return null;
                        }
                );
            }
        }
    }

    @Nonnull
    @Override
    @Step("Добавление в БД и возвращение списка {count} друзей пользователя: {targetUser.username}")
    public List<String> addFriendList(UserJson targetUser, int count) {
        List<String> friends = new ArrayList<>();
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepositoryJdbc.findById(targetUser.id()).orElseThrow();
            for (int i = 0; i < count; i++) {
                String username = randomUsername();
                xaTransactionTemplate.execute(() -> {
                    userdataUserRepositoryJdbc.addFriend(targetEntity, createNewUser(username, "12345"));
                    return null;
                });
                friends.add(username);  // Сохраняем имя нового друга
            }
        }
        return friends;
    }

    @Nonnull
    private UserEntity createNewUser(String username, String password) {
        AuthUserEntity authUser = authUserEntity(username, password);
        authUserRepository.create(authUser);
        return userdataUserRepositoryHibernate.create(userEntity(username));
    }

    @Nonnull
    private UserEntity userEntity(String username) {
        UserEntity ue = new UserEntity();
        ue.setUsername(username);
        ue.setCurrency(CurrencyValues.RUB);
        return ue;
    }

    @Nonnull
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

    @Nonnull
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

    @Nonnull
    public UserJson createUserSpringJdbcWithTransaction(UserJson user) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntityJson(user);

                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            udUserDaoSpring.create(UserEntity.fromJson(user)),
                            null
                    );
                }
        ));
    }

    @Nonnull
    public UserJson createUserSpringJdbcWithoutTransaction(UserJson user) {
        AuthUserEntity authUser = authUserEntityJson(user);

        authUserRepository.create(authUser);
        return UserJson.fromEntity(
                udUserDaoSpring.create(UserEntity.fromJson(user)),
                null
        );
    }

    @Nonnull
    @Step("Создание пользователя: {user.username}")
    public UserJson createUserJdbcWithTransaction(UserJson user) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntityJson(user);
                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            udUserDao.create(UserEntity.fromJson(user)),
                            null
                    );
                }
        ));
    }

    @Nonnull
    @Step("Создание пользователя: {user.username}")
    public UserJson createUserJdbcWithoutTransaction(UserJson user) {
        AuthUserEntity authUser = authUserEntityJson(user);
        authUserRepository.create(authUser);
        return UserJson.fromEntity(
                udUserDao.create(UserEntity.fromJson(user)),
                null
        );
    }

    @Step("Создание статуса дружбы между: {requester.username} и {addressee.username}")
    public void addFriend(UserJson requester, UserJson addressee) {
        userdataUserRepositoryHibernate.addFriend(UserEntity.fromJson(requester), UserEntity.fromJson(addressee));
    }

    @Step("Создание статуса приглашения в друзья: {requester.username} и {addressee.username}")
    public void addInvitation(UserJson requester, UserJson addressee) {
        userdataUserRepositoryHibernate.sendInvitation(UserEntity.fromJson(requester), UserEntity.fromJson(addressee));
    }
}
