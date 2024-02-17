package aydarss.fork.niffler.aypage;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

public class LoginPage extends BasePage<LoginPage> {

  private final SelenideElement loginInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitBtn = $("button[type='submit']");


  @Step("Вводим логин {login}")
  public LoginPage setLogin(String login) {
    loginInput.setValue(login);
    return this;
  }

  @Step("Вводим пароль {password}")
  public LoginPage setPassword(String password) {
    passwordInput.setValue(password);
    return this;
  }

  @Step("Нажимаем кнопку вход (submit)")
  public void submit() {
    submitBtn.click();
  }


  @Step("Вводим логин {user} и {password} и входим в систему")
  public LoginPage loginByUserAndPassword(String user, String password) {
    loginInput.setValue(user);
    passwordInput.setValue(password);
    submitBtn.click();
    return this;
  }
}
