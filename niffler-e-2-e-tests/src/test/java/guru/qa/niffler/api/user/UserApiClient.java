package guru.qa.niffler.api.user;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import java.io.IOException;
import java.util.List;

public class UserApiClient extends RestClient {

  private final UserApi userApi;

  public UserApiClient() {
    super(Config.getInstance().userUrl());
    this.userApi = retrofit.create(UserApi.class);
  }

  UserJson updateUserInfo(UserJson user) throws IOException {
    return userApi.updateUserInfo(user).execute().body();
  }

  UserJson currentUser(String username) throws IOException {
    return userApi.currentUser(username).execute().body();
  }

  List<UserJson> allUsers(String username) throws IOException {
    return userApi.allUsers(username).execute().body();
  }

}
