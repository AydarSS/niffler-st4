package aydarss.fork.niffler.aypage;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

public class MainPage extends BasePage<MainPage> {

  private final SelenideElement historyOfSpendings = $(".spendings-table tbody");
  private final SelenideElement deleteSpendingBtn = $(byText("Delete selected"));
  private final SelenideElement statisticsForm = $(".main-content__section-stats");

  
  public MainPage deleteFirstRowHistoryOfSpendingsByDescription (String description) {
    getFirstRowHistoryOfSpendingsByDescription(description).click();
    deleteSpendingBtn.click();
    return this;
  }

  public MainPage historyOfSpendingContentIsEmpty() {
    historyOfSpendings.$$("tr").shouldHave(size(0));
    return this;
  }

  public MainPage clickAllPeopleBtn() {
    $("a[href*='people']").click();
    return this;
  }

  public MainPage clickFriendsBtn() {
    $("a[href*='friends']").click();
    return this;
  }

  private SelenideElement getFirstRowHistoryOfSpendingsByDescription(String description) {
    return historyOfSpendings.$$("tr")
        .find(text(description))
        .$$("td")
        .first();
  }

  public MainPage checkThatStatisticDisplayed() {
    statisticsForm.should(visible);
    return this;
  }
}
