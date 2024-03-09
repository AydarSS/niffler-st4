package aydarss.fork.niffler.ayjupiter.ayextension;


import aydarss.fork.niffler.aydb.aymodel.Authority;
import aydarss.fork.niffler.aydb.aymodel.AuthorityEntity;
import aydarss.fork.niffler.aydb.aymodel.UserAuthEntity;
import aydarss.fork.niffler.aydb.aymodel.UserEntity;
import aydarss.fork.niffler.aydb.ayrepository.UserRepository;
import aydarss.fork.niffler.aydb.ayrepository.UserRepositoryHibernate;
import aydarss.fork.niffler.aydb.ayrepository.UserRepositorySJdbc;
import aydarss.fork.niffler.ayjupiter.MyTestUser;
import aydarss.fork.niffler.utils.DataUtils;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import java.util.Arrays;

public class MyDataBaseCreateUserExtension extends  MyCreateUserExtension{
  private static UserRepository userRepository = new UserRepositorySJdbc();

  @Override
  public UserJson createUser(MyTestUser user) {
    String username = user.username().isEmpty()
        ? DataUtils.generateRandomUsername()
        : user.username();
    String password = user.password().isEmpty()
        ? "12345"
        : user.password();

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

  @Override
  public UserJson createCategory(MyTestUser user, UserJson createdUser) {
    return null;
  }

  @Override
  public UserJson createSpend(MyTestUser user, UserJson createdUser) {
    return null;
  }
}
