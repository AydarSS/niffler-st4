package guru.qa.niffler.api.spend;

import guru.qa.niffler.api.CategoryApi;
import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.config.Config;

public class CategoryApiClient extends RestClient {

  private final CategoryApi categoryApi;

  public CategoryApiClient() {
    super(Config.getInstance().spendUrl());
    this.categoryApi = retrofit.create(CategoryApi.class);
  }
}
