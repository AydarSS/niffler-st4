package guru.qa.niffler.api.user;

import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.UserJson;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FriendsApi {


  @GET("/friends")
  Call<UserJson> friends(@Query("username") String username,
      @Query("includePending") boolean includePending);

  @GET("/invitations")
  Call<List<UserJson>> invitations(@Query("username") String username);

  @POST("/acceptInvitation")
  Call<List<UserJson>> acceptInvitation(@Body String username,
      @Body FriendJson invitation);

  @POST("/declineInvitation")
  Call<List<UserJson>> declineInvitation(@Body String username,
      @Body FriendJson invitation);

  @POST("/addFriend")
  Call<UserJson> addFriend(@Body String username,
      @Body FriendJson invitation);

  @DELETE("/removeFriend")
  Call<UserJson> removeFriend(@Query("username") String username,
      @Query("friendUsername") String friendUsername);

}
