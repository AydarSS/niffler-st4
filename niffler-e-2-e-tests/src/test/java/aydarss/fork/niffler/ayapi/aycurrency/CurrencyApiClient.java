package aydarss.fork.niffler.ayapi.aycurrency;

import aydarss.fork.niffler.ayapi.RestClient;
import aydarss.fork.niffler.ayconfig.Config;
import guru.qa.niffler.model.CurrencyCalculateJson;
import guru.qa.niffler.model.CurrencyJson;
import java.io.IOException;
import java.util.List;

public class CurrencyApiClient extends RestClient {

  private final CurrencyApi currencyApi;

  public CurrencyApiClient() {
    super(Config.getInstance().currencyUrl());
    this.currencyApi = retrofit.create(CurrencyApi.class);
  }

  List<CurrencyJson> getAllCurrencies() throws IOException {
    return currencyApi.getAllCurrencies().execute().body();
  }

  CurrencyCalculateJson getAllCurrenciesCalculate(CurrencyCalculateJson currencyCalculate)
      throws IOException {
    return currencyApi.getAllCurrenciesCalculate(currencyCalculate).execute().body();
  }
}
