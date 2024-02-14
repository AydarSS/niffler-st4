package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.MyGenerateSpend;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class MySpendingTest extends BaseWebTest {

  @MyGenerateSpend(
      username = "duck",
      description = "QA.GURU Advanced 4",
      amount = 72500.00,
      category = "Курсы",
      currency = CurrencyValues.RUB
  )
  @Test
  @Disabled
  void spendingShouldBeDeletedByButtonDeleteSpending(SpendJson spend) {
    Selenide.open("http://127.0.0.1:3000/main");
    new WelcomePage().clickLoginBtn();
    new LoginPage()
        .loginByUserAndPassword("duck", "12345");
    new MainPage().deleteFirstRowHistoryOfSpendingsByDescription(spend.description())
        .historyOfSpendingContentIsEmpty();

  }

}
