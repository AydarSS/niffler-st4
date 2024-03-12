package aydarss.fork.niffler.ayjupiter.ayextension;


import aydarss.fork.niffler.aydb.aymodel.Authority;
import aydarss.fork.niffler.aydb.aymodel.AuthorityEntity;
import aydarss.fork.niffler.aydb.aymodel.UserAuthEntity;
import aydarss.fork.niffler.aydb.aymodel.UserEntity;
import aydarss.fork.niffler.aydb.aymodel.ayspend.CategoryEntity;
import aydarss.fork.niffler.aydb.aymodel.ayspend.SpendEntity;
import aydarss.fork.niffler.aydb.ayrepository.FriendshipRepository;
import aydarss.fork.niffler.aydb.ayrepository.FriendshipRepositorySJdbc;
import aydarss.fork.niffler.aydb.ayrepository.UserRepository;
import aydarss.fork.niffler.aydb.ayrepository.UserRepositorySJdbc;
import aydarss.fork.niffler.aydb.ayrepository.ayspend.SpendRepository;
import aydarss.fork.niffler.aydb.ayrepository.ayspend.SpendRepositorySJdbc;
import aydarss.fork.niffler.ayjupiter.MyTestUser;
import aydarss.fork.niffler.ayjupiter.ayannotation.Friendship.FriendshipState;
import aydarss.fork.niffler.utils.DataUtils;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import java.util.Arrays;
import java.util.Date;

public class MyDataBaseCreateUserExtension extends MyCreateUserExtension {

  private static UserRepository userRepository = new UserRepositorySJdbc();
  private static SpendRepository spendRepository = new SpendRepositorySJdbc();
  private static FriendshipRepository friendshipRepository = new FriendshipRepositorySJdbc();

  @Override
  public UserJson createUser(MyTestUser user) {
    String username = user.username().isEmpty()
        ? DataUtils.generateRandomUsername()
        : user.username();
    String password = user.password().isEmpty()
        ? "12345"
        : user.password();

    return createUserWithUsernameAndPassword(username, password);
  }

  @Override
  public UserJson createCategory(MyTestUser user, UserJson createdUser) {
    Arrays.stream(user.categoryValue()).forEach(
        categoryToAdd -> {
          CategoryEntity categoryEntity = new CategoryEntity();
          categoryEntity.setCategory(categoryToAdd.category());
          categoryEntity.setUsername(createdUser.username());
          spendRepository.createCategory(categoryEntity);
        });
    return createdUser;
  }

  @Override
  public UserJson createSpend(MyTestUser user, UserJson createdUser) {
    Arrays.stream(user.spendValue())
        .forEach(spendToAdd -> {

          CategoryEntity categoryEntity = spendRepository.findCategoryByUserIdAndCategory(createdUser.username(),
              spendToAdd.category()).get();;

          SpendEntity spendEntity = new SpendEntity();
          spendEntity.setSpendDate(new Date());
          spendEntity.setCategory(categoryEntity);
          spendEntity.setUsername(createdUser.username());
          spendEntity.setCurrency(spendToAdd.currency());
          spendEntity.setAmount(spendToAdd.amount());
          spendEntity.setDescription(spendToAdd.description());
          spendRepository.createSpend(spendEntity);
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
              switch (friendship.friedshipState()) {
                case FRIENDS:
                  friendshipRepository.addFriendShip(createdUser.id(), createdFriend.id(), false);
                  break;
                case INCOME_REQUEST:
                  friendshipRepository.addFriendShip(createdFriend.id(), createdUser.id(), true);
                  break;
                case OUTCOME_REQUEST:
                  friendshipRepository.addFriendShip(createdUser.id(), createdFriend.id(), true);
                  break;
              }
            }
        );
  }

  private UserJson createUserWithUsernameAndPassword(String username, String password) {
    UserAuthEntity userAuth = new UserAuthEntity();
    userAuth.setUsername(username);
    userAuth.setPassword(password);
    userAuth.setEnabled(true);
    userAuth.setAccountNonExpired(true);
    userAuth.setAccountNonLocked(true);
    userAuth.setCredentialsNonExpired(true);
    AuthorityEntity[] authorities = Arrays.stream(Authority.values()).map(
        a -> {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setAuthority(a);
          return ae;
        }
    ).toArray(AuthorityEntity[]::new);

    userAuth.addAuthorities(authorities);

    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(username);
    userEntity.setCurrency(CurrencyValues.RUB);

    userRepository.createInAuth(userAuth);
    userRepository.createInUserdata(userEntity);
    return new UserJson(
        userEntity.getId(),
        userEntity.getUsername(),
        userEntity.getFirstname(),
        userEntity.getSurname(),
        guru.qa.niffler.model.CurrencyValues.valueOf(userEntity.getCurrency().name()),
        userEntity.getPhoto() == null ? "" : new String(userEntity.getPhoto()),
        null,
        new TestData(
            password,
            null,
            null
        )
    );
  }
}
