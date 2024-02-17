package aydarss.fork.niffler.aypage.utils.data;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import java.util.Date;
import java.util.Objects;

public class SpendForSpendingTable {

  private Date date;
  private Double amount;
  private CurrencyValues curency;
  private String category;
  private String description;

  public SpendForSpendingTable(Date date, Double amount, CurrencyValues curency,
      String description, String category) {
    this.date = date;
    this.amount = amount;
    this.curency = curency;
    this.category = category;
    this.description = description;
  }

  public static SpendForSpendingTable fromJson(SpendJson spendJson) {
    return new SpendForSpendingTable(
        spendJson.spendDate(),
        spendJson.amount(),
        spendJson.currency(),
        spendJson.category(),
        spendJson.description());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SpendForSpendingTable that = (SpendForSpendingTable) o;
    return Objects.equals(date, that.date) && Objects.equals(amount, that.amount)
        && curency == that.curency && Objects.equals(category, that.category)
        && Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, amount, curency, category, description);
  }

  @Override
  public String toString() {
    return "date=" + date +
        ", amount=" + amount +
        ", curency=" + curency +
        ", category='" + category + '\'' +
        ", description='" + description + '\'' +
        '}';
  }
}
