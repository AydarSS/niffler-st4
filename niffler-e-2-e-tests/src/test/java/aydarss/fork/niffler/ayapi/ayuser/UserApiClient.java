package aydarss.fork.niffler.ayapi.ayuser;

import aydarss.fork.niffler.ayapi.RestClient;
import aydarss.fork.niffler.ayconfig.Config;
import aydarss.fork.niffler.aymodel.UserJson;
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
