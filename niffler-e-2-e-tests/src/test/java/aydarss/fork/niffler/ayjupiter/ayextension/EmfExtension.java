package aydarss.fork.niffler.ayjupiter.ayextension;

import aydarss.fork.niffler.aydb.EmfProvider;
import jakarta.persistence.EntityManagerFactory;

public class EmfExtension implements SuiteExtension {

  @Override
  public void afterSuite() {
    EmfProvider.INSTANCE.storedEmf().forEach(
        EntityManagerFactory::close
    );
  }
}
