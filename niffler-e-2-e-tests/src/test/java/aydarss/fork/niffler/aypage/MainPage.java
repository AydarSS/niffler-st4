package aydarss.fork.niffler.aypage;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import aydarss.fork.niffler.aypage.aycomponent.SpendingTable;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

public class MainPage extends BasePage<MainPage> {

  private final SelenideElement historyOfSpendings = $(".spendings-table tbody");
  private final SelenideElement deleteSpendingBtn = $(byText("Delete selected"));
  private final SelenideElement statisticsForm = $(".main-content__section-stats");
  private final SpendingTable spendingTable = new SpendingTable();

  @Step("Удаляем трату {description}")
  public MainPage deleteFirstRowHistoryOfSpendingsByDescription(String description) {
    getFirstRowHistoryOfSpendingsByDescription(description).click();
    deleteSpendingBtn.click();
    return this;
  }

  @Step("Проверяем, что история трат пустая")
  public MainPage historyOfSpendingContentIsEmpty() {
    historyOfSpendings.$$("tr").shouldHave(size(0));
    return this;
  }

  @Step("Проверим, что статистика отображается")
  public MainPage checkThatStatisticDisplayed() {
    statisticsForm.should(visible);
    return this;
  }

  private SelenideElement getFirstRowHistoryOfSpendingsByDescription(String description) {
    return historyOfSpendings.$$("tr")
        .find(text(description))
        .$$("td")
        .first();
  }

  public SpendingTable getSpendingTable() {
    return spendingTable;
  }

}
