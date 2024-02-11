package guru.qa.niffler.api.user;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;

public class FriendsApiClient extends RestClient {

  private final FriendsApi friendsApi;

  public FriendsApiClient() {
    super(Config.getInstance().userUrl());
    this.friendsApi = retrofit.create(FriendsApi.class);
  }

}
