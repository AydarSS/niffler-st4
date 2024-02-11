package guru.qa.niffler.api.user;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;

public class UserApiClient extends RestClient {

  private final UserApi userApi;

  public UserApiClient() {
    super(Config.getInstance().userUrl());
    this.userApi = retrofit.create(UserApi.class);
  }
}
