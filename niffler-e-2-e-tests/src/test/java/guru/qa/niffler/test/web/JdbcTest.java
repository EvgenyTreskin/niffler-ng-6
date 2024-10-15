package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JdbcTest {

    @Test
    void txTest(){
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
    void springJdbcTest(){
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUser(
                new UserJson(
                        null,
                        "valentin-10",
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
    void withSpringJdbcTransactionTest(){
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserWithSpringJdbcTransaction(
                new UserJson(
                        null,
                        "valentin-31",
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
    void withoutSpringJdbcTransactionTest(){
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserWithoutSpringJdbcTransaction(
                new UserJson(
                        null,
                        "valentin-32",
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
    void withJdbcTransactionTest(){
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserWithJdbcTransaction(
                new UserJson(
                        null,
                        "valentin-33",
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
    void withoutJdbcTransactionTest(){
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserWithoutJdbcTransaction(
                new UserJson(
                        null,
                        "valentin-34",
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
    @Disabled("могут падать, сделаны для проверки некорректности использования ChainedTransactionManager")
    void withSpringJdbcChainedTransactionTest(){
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserSpringJdbcChainedTransaction(
                new UserJson(
                        null,
                        "valentin-15",
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

    //    могут падать, сделаны для проверки некорректности использования ChainedTransactionManager
    @Test
    @Disabled("могут падать, сделаны для проверки некорректности использования ChainedTransactionManager")
    void withJdbcChainedTransactionTest(){
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUserJdbcChainedTransaction(
                new UserJson(
                        null,
                        "valentin-16",
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

}
