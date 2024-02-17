package aydarss.fork.niffler.ayjupiter.ayextension;

import aydarss.fork.niffler.aydb.ayrepository.UserRepository;
import aydarss.fork.niffler.aydb.ayrepository.UserRepositoryHibernate;
import java.lang.reflect.Field;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

public class UserRepositoryExtension implements TestInstancePostProcessor {
  @Override
  public void postProcessTestInstance(Object o, ExtensionContext extensionContext) throws Exception {
    for (Field field : o.getClass().getDeclaredFields()) {
      if (field.getType().isAssignableFrom(UserRepository.class)) {
        field.setAccessible(true);
        field.set(o, new UserRepositoryHibernate());
      }
    }
  }
}
