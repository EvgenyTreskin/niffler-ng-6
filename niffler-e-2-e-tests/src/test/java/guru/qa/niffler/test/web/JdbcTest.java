package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;

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

    @Test
    void springJdbcTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUser(
                new UserJson(
                        null,
                        "valentin-1",
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
    void withSpringJdbcTransactionTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserWithSpringJdbcTransaction(
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
        UserJson user = usersDbClient.createUserWithoutSpringJdbcTransaction(
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
        UserJson user = usersDbClient.createUserWithJdbcTransaction(
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
        UserJson user = usersDbClient.createUserWithoutJdbcTransaction(
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
        UserJson requesterUser = usersDbClient.createUser(
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

        UserJson addresseeUser = usersDbClient.createUser(
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
        UserJson firstUser = usersDbClient.createUser(
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

        UserJson secondUser = usersDbClient.createUser(
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
}
