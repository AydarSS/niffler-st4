package aydarss.fork.niffler.ayjupiter.ayextension;

import aydarss.fork.niffler.ayapi.ayauth.AuthApiClientForMyApiLogin;
import aydarss.fork.niffler.ayapi.ayspend.CategoryApiClient;
import aydarss.fork.niffler.ayapi.ayspend.SpendApiClient;
import aydarss.fork.niffler.ayapi.ayuser.FriendsApiClient;
import aydarss.fork.niffler.ayapi.ayuser.UserApiClient;
import aydarss.fork.niffler.ayjupiter.MyTestUser;
import aydarss.fork.niffler.utils.DataUtils;
import com.google.common.base.Stopwatch;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.FriendJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;

public class MyRestCreateUserExtension extends MyCreateUserExtension {

  final AuthApiClientForMyApiLogin authApiClientForMyApiLogin = new AuthApiClientForMyApiLogin();
  final UserApiClient userApiClient = new UserApiClient();
  final CategoryApiClient categoryApiClient = new CategoryApiClient();
  final SpendApiClient spendApiClient = new SpendApiClient();
  final FriendsApiClient friendsApiClient = new FriendsApiClient();

  @SneakyThrows
  @Override
  public UserJson createUser(MyTestUser user) {
    String username = user.username().isEmpty()
        ? DataUtils.generateRandomUsername()
        : user.username();
    String password = user.password().isEmpty()
        ? "12345"
        : user.password();
   return createUserWithUsernameAndPassword(username,password);
  }

  @Override
  public UserJson createCategory(MyTestUser user, UserJson createdUser) {
    Arrays.stream(user.categoryValue()).forEach(categoryToAdd -> {
          CategoryJson categoryJson =
              new CategoryJson(null, categoryToAdd.category(), createdUser.username());
          try {
            categoryApiClient.createCategory(categoryJson);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
    );
    return createdUser;
  }

  @Override
  public UserJson createSpend(MyTestUser user, UserJson createdUser) {
    Arrays.stream(user.spendValue())
        .forEach(spendToAdd -> {
          SpendJson spendJson = new SpendJson(null,
              new Date(),
              spendToAdd.category(),
              spendToAdd.currency(),
              spendToAdd.amount(),
              spendToAdd.description(),
              createdUser.username());
          try {
            spendApiClient.addSpend(spendJson);
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
    return createdUser;

  }

  @Override
  public void createFriedship(MyTestUser user, UserJson createdUser) {
    Arrays.stream(user.withFriends())
        .forEach(friendship -> {
              String username = DataUtils.generateRandomUsername();
              String password = "12345";
              UserJson createdFriend = createUserWithUsernameAndPassword(username, password);
              try {
                switch (friendship.friedshipState()) {
                  case FRIENDS:
                      friendsApiClient.addFriend(createdUser.username(), new FriendJson(username));
                      friendsApiClient.acceptInvitation(username, new FriendJson(createdUser.username()));
                    break;
                  case INCOME_REQUEST:
                    friendsApiClient.addFriend(username,new FriendJson(createdUser.username()));
                    break;
                  case OUTCOME_REQUEST:
                    friendsApiClient.addFriend(createdUser.username(),new FriendJson(username));
                    break;
                }
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
        );
  }

  private UserJson createUserWithUsernameAndPassword(String username, String password) {
    authApiClientForMyApiLogin.doRegister(username, password);
    Stopwatch stopwatch = Stopwatch.createStarted();
    while (stopwatch.elapsed(TimeUnit.MILLISECONDS) < 15000) {
      try {
        UserJson userJson = userApiClient.currentUser(username);
        if (userJson != null) {
          UserJson userWithTestData = userJson.addTestData(new TestData(password, null, null));
          return userWithTestData;
        } else {
          try {
            Thread.sleep(100);
          } catch (Exception ignored) {
          }
        }
      } catch (Exception ignored) {
      }
    }
    throw new IllegalStateException("Не удалось получить пользователя из userdata");
  }
}
