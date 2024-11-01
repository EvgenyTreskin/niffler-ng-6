package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.impl.SpendDbClient;
import guru.qa.niffler.service.impl.UsersDbClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;
import java.util.Optional;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;
import static guru.qa.niffler.utils.RandomDataUtils.randomUserName;
import static org.junit.jupiter.api.Assertions.*;


public class JdbcTest {

    static UsersDbClient usersDbClient = new UsersDbClient();
    private final SpendDbClient spendDbClient = new SpendDbClient();

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
            "valentin-12",
            "valentin-13",
            "valentin-14"
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

    @Test
    void fullSpendAndCategoryLifecycleTest() {
        // Шаг 1: Создание категории
        CategoryJson category = spendDbClient.createCategory(
                new CategoryJson(
                        null,
                        randomCategoryName(),
                        "Valentin-full-lifecycle",
                        false
                )
        );
        assertNotNull(category.id(), "ID новой категории не должен быть null");
        System.out.println("Созданная категория: " + category);

        // Шаг 2: Обновление категории
        CategoryJson updatedCategory = new CategoryJson(
                category.id(),
                "updated-" + category.name(), // Обновленное имя категории
                category.username(),
                category.archived()
        );
        CategoryJson resultCategory = spendDbClient.updateCategory(updatedCategory);
        assertEquals("updated-" + category.name(), resultCategory.name(), "Имя категории должно быть обновлено");
        System.out.println("Обновленная категория: " + resultCategory);

        // Шаг 3: Создание траты с обновленной категорией
        SpendJson spend = spendDbClient.updateSpend(
                new SpendJson(
                        null,
                        new Date(),
                        resultCategory,
                        CurrencyValues.RUB,
                        1500.0,
                        "spend-name-lifecycle-test",
                        "Valentin-full-lifecycle"
                )
        );
        assertNotNull(spend.id(), "ID новой траты не должен быть null");
        System.out.println("Созданная трата: " + spend);

        // Шаг 4: Обновление траты - создание нового объекта SpendJson с обновленными значениями
        SpendJson updatedSpend = new SpendJson(
                spend.id(),
                spend.spendDate(),
                spend.category(),
                spend.currency(),
                2000.0, // Обновленная сумма
                "lifecycle-updated-spend-description", // Обновленное описание
                spend.username()
        );

        SpendJson resultSpend = spendDbClient.updateSpend(updatedSpend);
        assertEquals(2000.0, resultSpend.amount(), "Сумма должна быть обновлена");
        assertEquals("lifecycle-updated-spend-description", resultSpend.description(), "Описание должно быть обновлено");
        System.out.println("Обновленная трата: " + resultSpend);

        Optional<SpendJson> foundSpendById = spendDbClient.findSpendById(resultSpend.id());
        assertTrue(foundSpendById.isPresent(), "Трата должна быть найдена по ID");
        System.out.println("Найденная трата по ID: " + foundSpendById.get());

        Optional<SpendJson> foundSpendsByDescription = spendDbClient.findSpendByUsernameAndDescription(resultSpend.username(), resultSpend.description());
        assertFalse(foundSpendsByDescription.isEmpty(), "Список трат не должен быть пустым для указанного пользователя и описания");
        System.out.println("Найденные траты по описанию: " + foundSpendsByDescription);

        Optional<CategoryJson> foundCategoryById = spendDbClient.findCategoryById(resultCategory.id());
        assertTrue(foundCategoryById.isPresent(), "Категория должна быть найдена по ID");
        System.out.println("Найденная категория по ID: " + foundCategoryById.get());

        Optional<CategoryJson> foundCategoryByNameAndUsername = spendDbClient.findCategoryByUsernameAndCategoryName(resultSpend.username(), resultCategory.name());
        assertTrue(foundCategoryByNameAndUsername.isPresent(), "Категория должна быть найдена по имени пользователя и имени категории");
        System.out.println("Найденная категория по имени пользователя и имени категории: " + foundCategoryByNameAndUsername.get());

//        spendDbClient.deleteSpend(resultSpend);
//        Optional<SpendJson> foundSpendAfterDeletion = spendDbClient.findSpendById(resultSpend.id());
//        assertTrue(foundSpendAfterDeletion.isEmpty(), "Трата не должна быть найдена после удаления");
//        System.out.println("Трата успешно удалена: " + resultSpend);

//        spendDbClient.deleteCategory(resultCategory);
//        Optional<CategoryJson> foundCategoryAfterDeletion = spendDbClient.findCategoryById(resultCategory.id());
//        assertTrue(foundCategoryAfterDeletion.isEmpty(), "Категория не должна быть найдена после удаления");
//        System.out.println("Категория успешно удалена: " + resultCategory);
    }
}
