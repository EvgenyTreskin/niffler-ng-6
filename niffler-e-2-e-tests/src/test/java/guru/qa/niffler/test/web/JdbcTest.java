package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;

import static guru.qa.niffler.utils.RandomDataUtils.randomUserName;


public class JdbcTest {

    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-tx-6",
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spend-name-tx",
                        "duck"
                )
        );
        System.out.println(spend);
    }

    @ValueSource(strings = {
            "valentin-2",
            "valentin-3",
            "valentin-4"
    })
    @ParameterizedTest
    void springJdbcTest(String username) {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUser(
                username,
                "12345"
        );
    }

    @Test
    void withSpringJdbcTransactionTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserSpringJdbcWithTransaction(
                new UserJson(
                        null,
                        "valentin-2",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );
        System.out.println(user);
    }

    @Test
    void withoutSpringJdbcTransactionTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserSpringJdbcWithoutTransaction(
                new UserJson(
                        null,
                        "valentin-3",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );
        System.out.println(user);
    }

    @Test
    void withJdbcTransactionTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserJdbcWithTransaction(
                new UserJson(
                        null,
                        "valentin-4",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );
        System.out.println(user);
    }

    @Test
    void withoutJdbcTransactionTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserJdbcWithoutTransaction(
                new UserJson(
                        null,
                        "valentin-5",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );
        System.out.println(user);
    }

    @Test
    void addInvitationToFriendsTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson requesterUser = usersDbClient.createUserSpringJdbcWithTransaction(
                new UserJson(
                        null,
                        randomUserName(),
                        randomUserName(),
                        randomUserName(),
                        randomUserName(),
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                ));

        UserJson addresseeUser = usersDbClient.createUserSpringJdbcWithTransaction(
                new UserJson(
                        null,
                        randomUserName(),
                        randomUserName(),
                        randomUserName(),
                        randomUserName(),
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                ));

        usersDbClient.addInvitation(requesterUser, addresseeUser);
    }

    @Test
    void addFriendTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson firstUser = usersDbClient.createUserSpringJdbcWithTransaction(
                new UserJson(
                        null,
                        randomUserName(),
                        randomUserName(),
                        randomUserName(),
                        randomUserName(),
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                ));

        UserJson secondUser = usersDbClient.createUserSpringJdbcWithTransaction(
                new UserJson(
                        null,
                        randomUserName(),
                        randomUserName(),
                        randomUserName(),
                        randomUserName(),
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                ));

        usersDbClient.addFriend(firstUser, secondUser);
    }

    @ValueSource(strings = {
            "valentin-6"
    })
    @ParameterizedTest
    void addIncomeInvitationTest(String username) {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUser(
                username,
                "12345"
        );
        usersDbClient.addIncomeInvitation(user, 1);
        usersDbClient.addOutcomeInvitation(user, 1);
    }
}
