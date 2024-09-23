package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith(BrowserExtension.class)
public class ProfileTest {
  private static final Config CFG = Config.getInstance();
  private static final MainPage mainPage = new MainPage();

  @Category(
          username = "appollo",
          archived = false
  )
  @Test
  void activeCategoryShouldPresentInCategoriesList(CategoryJson categoryJson) {
    open(CFG.frontUrl(), LoginPage.class)
            .login("appollo", "12345");
    mainPage.checkStatisticAndHistoryOfSpendingAppear();
    mainPage.clickToProfileUser()
            .clickArchiveCategory(categoryJson.name())
            .clickCloseOrArchiveOrUnarchiveCategory("Archive")
            .checkNotCategoryByNameInProfile(categoryJson.name());
  }

  @Category(
          username = "appollo",
          archived = true
  )
  @Test
  void archiveCategoryShouldPresentInCategoriesList(CategoryJson categoryJson) {
    open(CFG.frontUrl(), LoginPage.class)
            .login("appollo", "12345");
    mainPage.checkStatisticAndHistoryOfSpendingAppear();
    mainPage.clickToProfileUser()
            .clickOnCheckboxShowArchived()
            .clickUnarchiveCategory(categoryJson.name())
            .clickCloseOrArchiveOrUnarchiveCategory("Unarchive")
            .clickOnCheckboxShowArchived()
            .checkCategoryByNameInProfile(categoryJson.name());
  }
}
