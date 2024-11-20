package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.BasePage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SearchField<T extends BasePage<?>> extends BaseComponent<T> {

    public SearchField(SelenideElement searchFieldElement, T page) {
        super(searchFieldElement, page);
    }

    @Nonnull
    @Step("Поиск по значению: {value}")
    public T search(String value) {
        self.setValue(value).pressEnter();
        return page;
    }

    @Nonnull
    @Step("Очистить строку поиска, если она не пустая")
    public SearchField<T> clearIfNotEmpty() {
        self.clear();
        return this;
    }
}
