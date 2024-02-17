package aydarss.fork.niffler.aycondition;

import aydarss.fork.niffler.aypage.utils.data.SpendForSpendingTable;
import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.ex.DoesNotContainTextsError;
import com.codeborne.selenide.impl.CollectionSource;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SpendCollectionCondition {

  public static CollectionCondition spends(SpendJson... expectedSPends) {
    return new CollectionCondition() {

      @Override
      public void fail(CollectionSource collection, CheckResult lastCheckResult,
          @Nullable Exception cause, long timeoutMs) {
        throw new DoesNotContainTextsError(
            collection,
            List.of(((List) lastCheckResult.getActualValue()).get(1).toString()),
            List.of(((List) lastCheckResult.getActualValue()).get(2).toString()),
            List.of(((List) lastCheckResult.getActualValue()).get(0).toString()),
            explanation,
            timeoutMs,
            cause);
      }

      @SneakyThrows
      @Nonnull
      @Override
      public CheckResult check(Driver driver, List<WebElement> elements) {
        if (elements.size() != expectedSPends.length) {
          return CheckResult.rejected("Incorrect table size", elements);
        }
        List<SpendForSpendingTable> spendsInTableData = new ArrayList<>();

        List<SpendForSpendingTable> spendsExpectedData = Arrays.stream(expectedSPends)
            .map(spendJson -> SpendForSpendingTable.fromJson(spendJson))
            .toList();

        for (WebElement element : elements) {
          List<WebElement> tds = element.findElements(By.cssSelector("td"));

          SpendForSpendingTable spendDataActual =
              new SpendForSpendingTable(
                  new SimpleDateFormat("dd MMM yy", Locale.ENGLISH).parse(tds.get(1).getText()),
                  Double.valueOf(tds.get(2).getText()),
                  CurrencyValues.valueOf(tds.get(3).getText()),
                  tds.get(4).getText(),
                  tds.get(5).getText());

          spendsInTableData.add(spendDataActual);
        }

        List<SpendForSpendingTable> spendsDifferenceBetweenActualAndExpected =
            new ArrayList<>(spendsExpectedData);
        spendsDifferenceBetweenActualAndExpected.removeAll(spendsInTableData);

        boolean checkPassed = spendsDifferenceBetweenActualAndExpected.isEmpty();

        if (checkPassed) {
          return CheckResult.accepted();
        } else {
          return CheckResult.rejected(
              "Incorrect spends content",
              List.of(spendsDifferenceBetweenActualAndExpected, spendsExpectedData,
                  spendsInTableData));
        }

      }

      @Override
      public boolean missingElementSatisfiesCondition() {
        return false;
      }
    };
  }
}
