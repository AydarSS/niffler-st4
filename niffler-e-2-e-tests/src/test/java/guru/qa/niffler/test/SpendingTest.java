package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class SpendingTest extends BaseWebTest {

  WelcomePage welcomePage = new WelcomePage();
  LoginPage loginPage = new LoginPage();
  MainPage mainPage = new MainPage();

  static {
    Configuration.browserSize = "1980x1024";
  }

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
  void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
    Selenide.open("http://127.0.0.1:3000/main");
    welcomePage.clickLoginBtn();
    loginPage.loginByUserAndPassword("duck","12345");
    mainPage.deleteFirstRowHistoryOfSpendingsByDescription(spend.description());
    mainPage.historyOfSpendingContentIsEmpty();

  }

}
