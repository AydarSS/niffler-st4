package aydarss.fork.niffler.aytest.grphql;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import aydarss.fork.niffler.ayjupiter.MyTestUser;
import aydarss.fork.niffler.ayjupiter.ayannotation.ApiLogin;
import aydarss.fork.niffler.ayjupiter.ayannotation.Friendship;
import aydarss.fork.niffler.ayjupiter.ayannotation.Friendship.FriendshipState;
import aydarss.fork.niffler.ayjupiter.ayannotation.GqlRequestFile;
import aydarss.fork.niffler.ayjupiter.ayannotation.GqlRequestFileConverted;
import aydarss.fork.niffler.ayjupiter.ayannotation.MyUser;
import aydarss.fork.niffler.ayjupiter.ayannotation.Token;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.gql.GqlRequest;
import guru.qa.niffler.model.gql.GqlUser;
import guru.qa.niffler.model.gql.GqlUsers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class GqlUsersTest extends BaseGraphQLTest {

  @Test
  @ApiLogin(user = @MyTestUser)
  void currentUserShouldBeReturned(@MyUser UserJson testUser,
                                   @Token String bearerToken,
                                   @GqlRequestFile("gql/currentUserQuery.json") GqlRequest request) throws Exception {

    final GqlUser response = gatewayGqlApiClient.currentUser(bearerToken, request);
    assertEquals(
        testUser.username(),
        response.getData().getUser().getUsername()
    );
  }

  @Test
  @ApiLogin(
      user = @MyTestUser
  )
  void usersShouldBeReturned(@Token String bearerToken,
      @GqlRequestFile("gql/usersQuery.json") GqlRequest request) throws Exception {

    final GqlUsers response = gatewayGqlApiClient.users(bearerToken, request);
    assertAll(
        () -> assertTrue(response.getData().getUsers().size() != 0)
    );
  }

  @Test
  @ApiLogin(
      user = @MyTestUser
  )
  void updateUser(@Token String bearerToken,
      @GqlRequestFile("gql/updateUserMutation.json") GqlRequest request) throws Exception {

    final GqlUser response = gatewayGqlApiClient.updateUser(bearerToken, request);
    assertAll(
        ()-> assertEquals("Pizzly", response.getData().getUpdateUser().getFirstname()),
        ()-> assertEquals("Pizzlyvich",  response.getData().getUpdateUser().getSurname())
    );
  }

  @Test
  @ApiLogin(
      user = @MyTestUser(
          withFriends ={
              @Friendship(friedshipState = FriendshipState.FRIENDS)
          })
  )
  void userWith2FriendsMustReturnError(
      @MyUser() UserJson user,
      @Token String bearerToken,
      @GqlRequestFile("gql/getFriends2FriedsSubQuery.json") GqlRequest request) throws Exception {

    final GqlUser response = gatewayGqlApiClient.getFriends(bearerToken, request);
    assertAll(
        ()-> assertEquals(1, response.getErrors().size()),
        ()-> assertEquals("Can`t fetch over 2 friends sub-queries", response.getErrors().get(0).message())
    );
  }

  @Test
  @ApiLogin(
      user = @MyTestUser(
          withFriends ={
              @Friendship(friedshipState = FriendshipState.INCOME_REQUEST)
      })
  )
  void userWithInvitation2FriendsMustReturnError(@Token String bearerToken,
      @GqlRequestFile("gql/getFriends2InvitationsSubQuery.json") GqlRequest request) throws Exception {

    final GqlUser response = gatewayGqlApiClient.getFriends(bearerToken, request);

    assertAll(
        ()-> assertEquals(1, response.getErrors().size()),
        ()-> assertEquals("Can`t fetch over 2 invitations sub-queries", response.getErrors().get(0).message())
    );
  }

  @Test
  @ApiLogin(
      user = @MyTestUser(
          withFriends ={
              @Friendship(friedshipState = FriendshipState.FRIENDS)
          })
  )
  void userWithFriends(@Token String bearerToken,
      @GqlRequestFile("gql/getFriendsQuery.json") GqlRequest request) throws Exception {

    final GqlUser response = gatewayGqlApiClient.getFriends(bearerToken, request);

    assertAll(
        ()-> assertEquals(1, response.getData().getUser().getFriends().size())
    );
  }

  @ParameterizedTest
  @ApiLogin(user = @MyTestUser)
  @CsvSource( {
      "gql/getFriends2FriedsSubQuery.json, Can`t fetch over 2 friends sub-queries",
      "gql/getFriends2InvitationsSubQuery.json, Can`t fetch over 2 invitations sub-queries"
  })
  void userWithFriendsMustReturnError(@GqlRequestFileConverted GqlRequest request,
      String expectedErrorMessage,
      @Token String bearerToken) throws Exception {

    final GqlUser response = gatewayGqlApiClient.getFriends(bearerToken, request);

    assertAll(
        ()-> assertEquals(1, response.getErrors().size()),
        ()-> assertEquals(expectedErrorMessage, response.getErrors().get(0).message())
    );
  }

}
