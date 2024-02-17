package aydarss.fork.niffler.aypage;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

public class WelcomePage extends BasePage<WelcomePage> {

  private SelenideElement loginBtn = $("a:nth-child(1)");
  private SelenideElement registerBtn = $("a:nth-child(2)");

  @Step("Нажимаем на кнопку вход")
  public void clickLoginBtn() {
    loginBtn.click();
  }

  @Step("Нажимаем на кнопку решистрация")
  public void clickRegisterBtn() {
    registerBtn.click();
  }

}
