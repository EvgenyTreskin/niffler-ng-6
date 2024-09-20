package guru.qa.niffler.test.web;

import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;


@ExtendWith(BrowserExtension.class)
public class SignUpWebTest {

    private static final Config CFG = Config.getInstance();
    private static final Faker faker = new Faker();

    private final String login = faker.name().username();
    private final String password = faker.internet().password(3, 12);

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        open(CFG.frontUrl(), LoginPage.class)
                .createAccount()
                .unSucceedSignUp("lucky", "qwerty", "123456");
    }

    @Test
    void shouldNotRegisterUserWithExistingUsernameAndShowError() {
        open(CFG.frontUrl(), LoginPage.class)
                .createAccount()
                .unSucceedSignUp("duck", "qwerty", "qwerty");
    }

    @Test
    void shouldRegisterAndLoginNewUser() {
//        String login = Utils.generateRandomString(5);
//        String password = Utils.generateRandomString(5);
        open(CFG.frontUrl(), LoginPage.class)
                .createAccount()
                .succeedSignUp(login, password, password)
                .signIn()
                .login(login, password)
                .checkStatisticAndHistoryOfSpendingAppear();
    }
}