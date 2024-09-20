package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class LoginTest {

  private static final Config CFG = Config.getInstance();

  @Test
  void mainPageShouldBeDisplayedAfterSuccessLogin() {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
            .login("duck", "12345")
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