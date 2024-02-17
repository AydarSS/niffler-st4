package aydarss.fork.niffler.aypage;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

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

  @Step("Вводим в поле пользователь {usernameValue}")
  public RegisterPage setUsername(String usernameValue) {
    username.setValue(usernameValue);
    return this;
  }

  @Step("Вводим в поле пароль {passwordValue}")
  public RegisterPage setPassword(String passwordValue) {
    password.setValue(passwordValue);
    return this;
  }

  @Step("Вводим в поле повторить пароль {passwordValue}")
  public RegisterPage setPasswordSubmit(String passwordValue) {
    passwordSubmit.setValue(passwordValue);
    return this;
  }

  @Step("Нажимаем submit")
  public RegisterPage clickSubmitBtn() {
    submitBtn.click();
    return this;
  }

  @Step("Нажимаем submit пользователь существует в системе")
  public RegisterPage clickSubmitBtnForExistUser() {
    submitBtn.click();
    return this;
  }

  @Step("Проверяем сообщение, что пользователь уже существует в системе")
  public void checkUserAlreadyExistsMsg() {
    userAlreadyExistsMsg.shouldBe(Condition.visible);
  }

  @Step("Проверяем сообщение, что пользователь успешно зарегистрирован")
  public void checkregisterSuccessMsg() {
    registerSuccessMsg.shouldBe(Condition.visible);
  }

  @Step("Проверяем сообщение, что пароли не совпадают")
  public void checkPasswordsNotEqualMsg() {
    passwordsNotEqualMsg.shouldBe(Condition.visible);
  }

  @Step("Проверяем сообщение, пароль не соотсветсвует формату")
  public void checkPasswordLengthMsg() {
    passwordsLengthMsg.shouldBe(Condition.visible);
  }

}
