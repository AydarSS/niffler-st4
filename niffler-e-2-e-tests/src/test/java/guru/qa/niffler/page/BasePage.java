package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.message.Msg;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T extends BasePage> {

  protected final SelenideElement toaster = $(".Toastify__toast-body");

  protected final Header header = new Header();

  @SuppressWarnings("unchecked")
  @Step("")
  public T checkMessage(Msg msg) {
    toaster.shouldHave(text(msg.getMessage()));
    return (T) this;
  }

  public Header getHeader() {
    return header;
  }
}
