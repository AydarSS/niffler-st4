package aydarss.fork.niffler.aytest;

import aydarss.fork.niffler.ayjupiter.ayannotation.MyGenerateSpend;
import aydarss.fork.niffler.aymodel.CurrencyValues;
import aydarss.fork.niffler.aymodel.SpendJson;
import aydarss.fork.niffler.aypage.LoginPage;
import aydarss.fork.niffler.aypage.MainPage;
import aydarss.fork.niffler.aypage.WelcomePage;
import com.codeborne.selenide.Selenide;
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
