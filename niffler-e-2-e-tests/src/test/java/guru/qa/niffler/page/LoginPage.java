package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class LoginPage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement newAccountButton = $("a.form__register");
    private final SelenideElement badCredentialError = $("p.form__error");

    public MainPage login(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        return new MainPage();
    }

    public void unSucceedLogin(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        badCredentialError.should(appear);
    }

    public SignUpPage createAccount() {
        newAccountButton.click();
        return new SignUpPage();
    }
}
