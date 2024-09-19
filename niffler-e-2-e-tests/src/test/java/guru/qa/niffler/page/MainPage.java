package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement historyOfSpending = $x("//h2[contains(text(), 'History of Spendings')]");
    private final SelenideElement statistics = $("canvas[role='img']");
    private final SelenideElement personIcon = $("[data-testid='PersonIcon']");
    private final SelenideElement personMenu = $("[role='menu']");
    private final SelenideElement profileButton = $(byText("Profile"));
    private final SelenideElement imageInput = $(".image__input-label");

//try to correct PR
    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).shouldBe(visible);
    }

    public void checkStatisticAndHistoryOfSpendingAppear() {
        historyOfSpending.shouldBe(visible);
        statistics.shouldBe(visible);
    }

    public ProfilePage clickToProfileUser(){
        personIcon.click();
        personMenu.shouldBe(visible);
        profileButton.click();
        imageInput.shouldBe(visible);

        return new ProfilePage();
    }

}
