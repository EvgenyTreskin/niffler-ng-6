package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selenide.$;

public class SignUpPage {

    private final SelenideElement setUsername = $("input[name='username']");
    private final SelenideElement setPassword = $("input[name='password']");
    private final SelenideElement setPasswordSubmit = $("input[name='passwordSubmit']");
    private final SelenideElement submitRegistration = $("button[type='submit']");
    private final SelenideElement errorForm = $("span.form__error");


    public void unSucceedSignUp(String username, String password, String passwordRepeat) {
        setUsername.setValue(username);
        setPassword.setValue(password);
        setPasswordSubmit.setValue(passwordRepeat);
        submitRegistration.click();
        errorForm.should(appear);
        new SignUpPage();
    }

    public SignInPage succeedSignUp(String username, String password, String passwordRepeat) {
        setUsername.setValue(username);
        setPassword.setValue(password);
        setPasswordSubmit.setValue(passwordRepeat);
        submitRegistration.click();
        return new SignInPage();
    }
}