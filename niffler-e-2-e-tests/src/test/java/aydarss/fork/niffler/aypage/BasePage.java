package aydarss.fork.niffler.aypage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

import aydarss.fork.niffler.ayconfig.Config;
import aydarss.fork.niffler.aypage.aymessage.Msg;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

public abstract class BasePage<T extends BasePage> {

  protected static final Config CFG = Config.getInstance();

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
