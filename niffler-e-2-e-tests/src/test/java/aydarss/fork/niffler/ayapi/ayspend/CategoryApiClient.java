package aydarss.fork.niffler.ayapi.ayspend;

import aydarss.fork.niffler.ayapi.CategoryApi;
import aydarss.fork.niffler.ayapi.RestClient;
import aydarss.fork.niffler.ayconfig.Config;
import aydarss.fork.niffler.aymodel.CategoryJson;
import io.qameta.allure.Step;
import java.io.IOException;
import java.util.List;

public class CategoryApiClient extends RestClient {

  private final CategoryApi categoryApi;

  public CategoryApiClient() {
    super(Config.getInstance().spendUrl());
    this.categoryApi = retrofit.create(CategoryApi.class);
  }

  @Step("Create category")
  public CategoryJson createCategory(CategoryJson category) throws IOException {
    return categoryApi.addCategory(category)
        .execute()
        .body();
  }

  @Step("Get categories for user '{userName}'")
  public List<CategoryJson> getCategories(String userName) throws IOException {
    return categoryApi.getCategories(userName)
        .execute()
        .body();
  }
}
