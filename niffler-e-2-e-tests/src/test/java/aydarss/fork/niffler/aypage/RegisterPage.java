package aydarss.fork.niffler.aypage;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

public class RegisterPage extends BasePage<RegisterPage> {

  private final SelenideElement username = $("#username");
  private final SelenideElement password = $("#password");
  private final SelenideElement passwordSubmit = $("#passwordSubmit");
  private final SelenideElement submitBtn = $("button[type='submit']");
  private final SelenideElement mainBtn = $("a[href*='redirect']");
  private final SelenideElement userAlreadyExistsMsg = $(byText("Username `duck` already exists"));
  private final SelenideElement registerSuccessMsg = $(byText("Congratulations! You've registered!"));
  private final SelenideElement passwordsNotEqualMsg = $(byText("Passwords should be equal"));
  private final SelenideElement passwordsLengthMsg = $(byText("Allowed password length should be from 3 to 12 characters"));


  public RegisterPage setUsername(String usernameValue) {
    username.setValue(usernameValue);
    return this;
  }

  public RegisterPage setPassword(String passwordValue) {
    password.setValue(passwordValue);
    return this;
  }

  public RegisterPage setPasswordSubmit(String passwordValue) {
    passwordSubmit.setValue(passwordValue);
    return this;
  }

  public RegisterPage clickSubmitBtn() {
    submitBtn.click();
    return this;
  }

  public RegisterPage clickSubmitBtnForExistUser() {
    submitBtn.click();
    return this;
  }

  public void checkUserAlreadyExistsMsg() {
    userAlreadyExistsMsg.shouldBe(Condition.visible);
  }

  public void checkregisterSuccessMsg() {
    registerSuccessMsg.shouldBe(Condition.visible);
  }

  public void checkPasswordsNotEqualMsg() {
    passwordsNotEqualMsg.shouldBe(Condition.visible);
  }

  public void checkPasswordLengthMsg() {
    passwordsLengthMsg.shouldBe(Condition.visible);
  }

}
