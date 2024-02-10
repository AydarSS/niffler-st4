package guru.qa.niffler.page;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

public class WelcomePage  extends BasePage<WelcomePage> {

  private SelenideElement loginBtn = $("a:nth-child(1)");
  private SelenideElement registerBtn = $("a:nth-child(2)");

  public void clickLoginBtn() {
    loginBtn.click();
  }

  public void clickRegisterBtn() {
    registerBtn.click();
  }

}
