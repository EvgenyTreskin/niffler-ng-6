package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class LoginTest {

    private static final Config CFG = Config.getInstance();

    @User(
            categories = {
                    @Category(name = "cat_1", archived = false),
                    @Category(name = "cat_2", archived = true),
            },
            spendings = {
                    @Spending(
                            category = "cat_3", description = "test_spend", amount = 100
                    )
            }
    )
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkStatisticAndHistoryOfSpendingAppear();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentialLoginAndShowError() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .unSucceedLogin("duck", "qwerty");
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentialPasswordAndShowError() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .unSucceedLogin("duckyyy", "12345");
    }
}