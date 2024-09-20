package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class SignInPage {

    private final SelenideElement signInButton = $("a.form_sign-in");

    public LoginPage signIn() {
        signInButton.click();
        return new LoginPage();
    }
}