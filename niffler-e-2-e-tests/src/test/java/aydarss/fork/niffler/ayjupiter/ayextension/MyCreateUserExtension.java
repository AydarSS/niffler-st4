package aydarss.fork.niffler.ayjupiter.ayextension;

import aydarss.fork.niffler.ayjupiter.MyTestUser;
import aydarss.fork.niffler.ayjupiter.ayannotation.ApiLogin;
import aydarss.fork.niffler.ayjupiter.ayannotation.MyTestUsers;
import aydarss.fork.niffler.ayjupiter.ayannotation.MyUser;
import guru.qa.niffler.model.UserJson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public abstract class MyCreateUserExtension implements BeforeEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace CREATE_USER_NAMESPACE
      = ExtensionContext.Namespace.create(MyCreateUserExtension.class);

  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    Map<MyUser.Point, List<MyTestUser>> usersForTest = extractUsersForTest(extensionContext);

    Map<MyUser.Point, List<UserJson>> createdUsers = new HashMap<>();
    for (Map.Entry<MyUser.Point, List<MyTestUser>> userInfo : usersForTest.entrySet()) {
      List<UserJson> usersForPoint = new ArrayList<>();
      for (MyTestUser myTestUser : userInfo.getValue()) {
        usersForPoint.add(createUser(myTestUser));
      }
      createdUsers.put(userInfo.getKey(), usersForPoint);
    }

    extensionContext.getStore(CREATE_USER_NAMESPACE)
        .put(extensionContext.getUniqueId(), createdUsers);
  }

  public abstract UserJson createUser(MyTestUser user);

  public abstract UserJson createCategory(MyTestUser user, UserJson createdUser);

  public abstract UserJson createSpend(MyTestUser user, UserJson createdUser);

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return AnnotationSupport.findAnnotation(parameterContext.getParameter(), MyUser.class).isPresent()
        &&
        (parameterContext.getParameter().getType().isAssignableFrom(UserJson.class) ||
            parameterContext.getParameter().getType().isAssignableFrom(UserJson[].class));
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    MyUser user = AnnotationSupport.findAnnotation(parameterContext.getParameter(), MyUser.class).get();
    Map<MyUser.Point, List<UserJson>> createdUsers = extensionContext.getStore(CREATE_USER_NAMESPACE).get(extensionContext.getUniqueId(), Map.class);
    List<UserJson> userJsons = createdUsers.get(user.value());
    if (parameterContext.getParameter().getType().isAssignableFrom(UserJson[].class)) {
      return userJsons.stream().toList().toArray(new UserJson[0]);
    } else {
      return userJsons.get(0);
    }
  }

  private Map<MyUser.Point, List<MyTestUser>> extractUsersForTest(ExtensionContext context) {
    Map<MyUser.Point, List<MyTestUser>> result = new HashMap<>();
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), ApiLogin.class).ifPresent(
        apiLogin -> {
          MyTestUser user = apiLogin.user();
          if (!user.fake()) {
            result.put(MyUser.Point.INNER, List.of(user));
          }
        }
    );
    List<MyTestUser> outerUsers = new ArrayList<>();
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), MyTestUser.class).ifPresent(
        tu -> {
          if (!tu.fake()) outerUsers.add(tu);
        }
    );
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), MyTestUsers.class).ifPresent(
        testUsers -> Arrays.stream(testUsers.value())
            .filter(tu -> !tu.fake())
            .forEach(outerUsers::add)
    );
    result.put(MyUser.Point.OUTER, outerUsers);
    return result;
  }

}
