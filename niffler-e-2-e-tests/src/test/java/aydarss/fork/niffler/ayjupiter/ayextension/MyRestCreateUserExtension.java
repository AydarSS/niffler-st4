package aydarss.fork.niffler.ayjupiter.ayextension;

import aydarss.fork.niffler.ayjupiter.MyTestUser;
import guru.qa.niffler.model.UserJson;

public class MyRestCreateUserExtension extends MyCreateUserExtension{

  @Override
  public UserJson createUser(MyTestUser user) {
    return null;
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
