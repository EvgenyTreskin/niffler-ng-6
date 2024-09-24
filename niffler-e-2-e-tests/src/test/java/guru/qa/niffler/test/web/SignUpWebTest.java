package guru.qa.niffler.test.web;

import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;


@ExtendWith(BrowserExtension.class)
public class SignUpWebTest {

    private static final Config CFG = Config.getInstance();

    private final String login = RandomDataUtils.randomUserName();
    private final String password = RandomDataUtils.randomPassword();

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
        open(CFG.frontUrl(), LoginPage.class)
                .createAccount()
                .succeedSignUp(login, password, password)
                .signIn()
                .login(login, password)
                .checkStatisticAndHistoryOfSpendingAppear();
    }
}