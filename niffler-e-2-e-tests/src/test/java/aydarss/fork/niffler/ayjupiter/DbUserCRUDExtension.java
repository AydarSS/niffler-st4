package aydarss.fork.niffler.ayjupiter;

import aydarss.fork.niffler.aydb.aymodel.Authority;
import aydarss.fork.niffler.aydb.aymodel.AuthorityEntity;
import aydarss.fork.niffler.aydb.aymodel.UserAuthEntity;
import aydarss.fork.niffler.aydb.aymodel.UserEntity;
import aydarss.fork.niffler.aydb.ayrepository.UserRepository;
import aydarss.fork.niffler.aydb.ayrepository.UserRepositoryJdbc;
import aydarss.fork.niffler.aydb.ayrepository.UserRepositorySJdbc;
import guru.qa.niffler.model.CurrencyValues;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public class DbUserCRUDExtension implements ParameterResolver, BeforeEachCallback,
    AfterTestExecutionCallback {

  public static final ExtensionContext.Namespace NAMESPACE
      = ExtensionContext.Namespace.create(DbUserCRUDExtension.class);

  private final UserRepository userRepository = getUserRepository();

  private final String fakeUserPassword = "12345";

  static String userAuthKey = "userAuthDB";
  static String userdataKey = "userdataDB";

  @Override
  public void beforeEach(ExtensionContext extensionContext) {
    List<Method> methods = new ArrayList<>();
    Arrays.stream(extensionContext.getRequiredTestClass().getDeclaredMethods())
        .filter(m -> m.isAnnotationPresent(BeforeEach.class))
        .forEach(methods::add);
    methods.add(extensionContext.getRequiredTestMethod());

    Optional<Method> methodAnnotatedByUser = methods
        .stream()
        .filter(method -> method.isAnnotationPresent(MyDbUser.class))
        .findFirst();

    if (methodAnnotatedByUser.isEmpty()) {
      return;
    }

    UserAuthEntity userAuthEntity = new UserAuthEntity();
    UserEntity userEntity = new UserEntity();

    MyDbUser myDbUser = methodAnnotatedByUser.get().getAnnotation(MyDbUser.class);

    String fakeRandomUser = UUID.randomUUID().toString();

    userEntity.setUsername(myDbUser.username().equals("") ? fakeRandomUser : myDbUser.username());
    userEntity.setCurrency(CurrencyValues.RUB);
    userRepository.createInUserdata(userEntity);

    userAuthEntity.setUsername(
        myDbUser.username().equals("") ? fakeRandomUser : myDbUser.username());
    userAuthEntity.setPassword(
        myDbUser.password().equals("") ? fakeUserPassword : myDbUser.password());
    userAuthEntity.setEnabled(true);
    userAuthEntity.setAccountNonExpired(true);
    userAuthEntity.setAccountNonLocked(true);
    userAuthEntity.setCredentialsNonExpired(true);
    userAuthEntity.setAuthorities(Arrays.stream(Authority.values())
        .map(e -> {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setAuthority(e);
          return ae;
        }).toList()
    );

    userRepository.createInAuth(userAuthEntity);

    Map<String, Object> userData = new HashMap<>();
    userData.put(userAuthKey, userAuthEntity);
    userData.put(userdataKey, userEntity);
    extensionContext.getStore(NAMESPACE).put(extensionContext.getUniqueId(), userData);
  }

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) {
    Map userDataMap = extensionContext.getStore(NAMESPACE)
        .get(extensionContext.getUniqueId(), Map.class);
    UserEntity userEntity = (UserEntity) userDataMap.get(userdataKey);
    UserAuthEntity userAuthEntity = (UserAuthEntity) userDataMap.get(userAuthKey);

    userRepository.deleteInAuthById(userAuthEntity.getId());
    userRepository.deleteInUserdataById(userEntity.getId());

  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return
        AnnotationSupport.findAnnotation(extensionContext.getRequiredTestMethod(), MyDbUser.class)
            .isPresent() &&
            parameterContext.getParameter().getType().isAssignableFrom(UserAuthEntity.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    Map users = extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
    return users.get(userAuthKey);
  }

  private static UserRepository getUserRepository() {
    String repositoryType = System.getProperty("repository", "sjdbc");
    if ("jdbc".equals(repositoryType)) {
      System.out.println("uses UserRepositoryJdbc");
      return new UserRepositoryJdbc();
    } else {
      System.out.println("uses UserRepositorySJdbc");
      return new UserRepositorySJdbc();
    }
  }
}
