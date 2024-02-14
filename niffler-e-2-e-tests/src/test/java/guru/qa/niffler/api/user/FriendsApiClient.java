package guru.qa.niffler.api.user;

import guru.qa.niffler.api.RestClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.UserJson;
import java.io.IOException;
import java.util.List;

public class FriendsApiClient extends RestClient {

  private final FriendsApi friendsApi;

  public FriendsApiClient() {
    super(Config.getInstance().userUrl());
    this.friendsApi = retrofit.create(FriendsApi.class);
  }

  UserJson friends(String username, boolean includePending) throws IOException {
    return friendsApi.friends(username, includePending).execute().body();
  }

  List<UserJson> invitations(String username) throws IOException {
    return friendsApi.invitations(username).execute().body();
  }

  List<UserJson> acceptInvitation(String username, FriendJson invitation) throws IOException {
    return friendsApi.acceptInvitation(username, invitation).execute().body();
  }

  List<UserJson> declineInvitation(String username, FriendJson invitation) throws IOException {
    return friendsApi.declineInvitation(username, invitation).execute().body();
  }

  UserJson addFriend(String username, FriendJson invitation) throws IOException {
    return friendsApi.addFriend(username, invitation).execute().body();
  }

  UserJson removeFriend(String username, String friendUsername) throws IOException {
    return friendsApi.removeFriend(username, friendUsername).execute().body();
  }

}
