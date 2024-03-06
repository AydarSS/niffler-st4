package aydarss.fork.niffler.aytest.web;

import aydarss.fork.niffler.ayjupiter.GenerateCategory;
import aydarss.fork.niffler.ayjupiter.ayannotation.GenerateSpend;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import java.text.SimpleDateFormat;
import java.util.Locale;
import lombok.SneakyThrows;
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

  @SneakyThrows
  @Test
  @DisplayName("Проверяем заполнение таблицы трат")
  void spendingTableShouldBeShowActualSpends() {
    SpendJson spendFirst = new SpendJson
        (null,
            new SimpleDateFormat("dd MMM yy", Locale.ENGLISH).parse("17 Feb 24"),
            "Обучение",
            CurrencyValues.RUB,
            20000d,
            "Обучение qa guru",
            "rabbit");

    SpendJson spendSecond = new SpendJson
        (null,
            new SimpleDateFormat("dd MMM yy", Locale.ENGLISH).parse("17 Feb 24"),
            "Наука",
            CurrencyValues.RUB,
            58000d,
            "Исследование",
            "rabbit");

    SpendJson spendThird = new SpendJson
        (null,
            new SimpleDateFormat("dd MMM yy", Locale.ENGLISH).parse("17 Feb 24"),
            "Одежда",
            CurrencyValues.RUB,
            89000d,
            "Жакет1",
            "rabbit");

    Selenide.open("http://127.0.0.1:3000/main");
    welcomePage.clickLoginBtn();
    loginPage.loginByUserAndPassword("rabbit", "12345");
    mainPage.
        getSpendingTable()
        .checkTableContains(spendFirst, spendSecond, spendThird);

  }

}
