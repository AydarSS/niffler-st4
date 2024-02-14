package guru.qa.niffler.api.currency;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;
import javax.annotation.Nonnull;

public class CurrencyApiClient extends RestClient {

  private final CurrencyApi currencyApi;

  public CurrencyApiClient() {
    super(Config.getInstance().currencyUrl());
    this.currencyApi = retrofit.create(CurrencyApi.class);
  }
}
