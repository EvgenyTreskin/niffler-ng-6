package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class SignUpTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createAccount()
                .unSucceedSignUp("lucky", "qwerty", "123456");
    }

    @Test
    void shouldNotRegisterUserWithExistingUsernameAndShowError() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createAccount()
                .unSucceedSignUp("duck", "qwerty", "qwerty");
    }

    @Test
    void shouldRegisterAndLoginNewUser() {
        String login = Utils.generateRandomString(5);
        String password = Utils.generateRandomString(5);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .createAccount()
                .succeedSignUp(login, password, password)
                .signIn()
                .login(login, password)
                .checkStatisticAndHistoryOfSpendingAppear();
    }
}
