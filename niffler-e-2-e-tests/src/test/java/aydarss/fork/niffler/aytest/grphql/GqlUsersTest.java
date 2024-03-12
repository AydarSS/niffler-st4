package aydarss.fork.niffler.aytest.grphql;

import aydarss.fork.niffler.ayjupiter.MyTestUser;
import aydarss.fork.niffler.ayjupiter.ayannotation.ApiLogin;
import aydarss.fork.niffler.ayjupiter.ayannotation.GqlRequestFile;
import aydarss.fork.niffler.ayjupiter.ayannotation.MyUser;
import aydarss.fork.niffler.ayjupiter.ayannotation.Token;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.model.gql.GqlRequest;
import guru.qa.niffler.model.gql.GqlUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GqlUsersTest extends BaseGraphQLTest {

  @Test
  @ApiLogin(user = @MyTestUser)
  void currentUserShouldBeReturned(@MyUser UserJson testUser,
                                   @Token String bearerToken,
                                   @GqlRequestFile("gql/currentUserQuery.json") GqlRequest request) throws Exception {

    final GqlUser response = gatewayGqlApiClient.currentUser(bearerToken, request);
    Assertions.assertEquals(
        testUser.username(),
        response.getData().getUser().getUsername()
    );
  }

}
