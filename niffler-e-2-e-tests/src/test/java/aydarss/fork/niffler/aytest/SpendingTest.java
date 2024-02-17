package aydarss.fork.niffler.aytest;

import aydarss.fork.niffler.ayjupiter.GenerateCategory;
import aydarss.fork.niffler.ayjupiter.ayannotation.GenerateSpend;
import aydarss.fork.niffler.aymodel.CurrencyValues;
import aydarss.fork.niffler.aymodel.SpendJson;
import aydarss.fork.niffler.aypage.LoginPage;
import aydarss.fork.niffler.aypage.MainPage;
import aydarss.fork.niffler.aypage.WelcomePage;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SpendingTest extends BaseWebTest {

  @GenerateCategory(
      username = "duck",
      category = "Медицина"
  )
  @GenerateSpend(
      username = "duck",
      description = "QA.GURU Advanced 4",
      amount = 72500.00,
      category = "Обучение",
      currency = CurrencyValues.RUB
  )
  @Test
  @Disabled
  @DisplayName("Категория должна удаляться по кнопке удалить и не отображаться")
  void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
    Selenide.open("http://127.0.0.1:3000/main");
    welcomePage.clickLoginBtn();
    loginPage.loginByUserAndPassword("duck", "12345");
    mainPage.deleteFirstRowHistoryOfSpendingsByDescription(spend.description());
    mainPage.historyOfSpendingContentIsEmpty();

  }

}
