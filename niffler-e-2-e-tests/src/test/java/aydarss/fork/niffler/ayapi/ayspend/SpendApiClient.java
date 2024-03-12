package aydarss.fork.niffler.ayapi.ayspend;

import aydarss.fork.niffler.ayapi.RestClient;
import aydarss.fork.niffler.ayconfig.Config;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticJson;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class SpendApiClient extends RestClient {

  private final SpendApi spendApi;

  public SpendApiClient() {
    super(Config.getInstance().spendUrl());
    this.spendApi = retrofit.create(SpendApi.class);
  }

  public SpendJson addSpend(SpendJson spend) throws IOException {
    return spendApi.addSpend(spend).execute().body();
  }

  List<SpendJson> getSpends(String username,
      CurrencyValues filterCurrency,
      Date from,
      Date to) throws IOException {
    return spendApi.getSpends(username, filterCurrency, from, to).execute().body();
  }

  List<StatisticJson> getStatistic(String username, CurrencyValues filterCurrency,
      Date from,
      Date to) throws IOException {
    return spendApi.getStatistic(username, filterCurrency, from, to).execute().body();
  }

  SpendJson editSpend(SpendJson spend) throws IOException {
    return spendApi.editSpend(spend).execute().body();
  }

  void deleteSpends(String username, List<String> ids) {
    spendApi.deleteSpends(username, ids);
  }

}
