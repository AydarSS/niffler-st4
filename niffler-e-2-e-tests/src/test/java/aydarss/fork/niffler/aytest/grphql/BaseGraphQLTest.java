package aydarss.fork.niffler.aytest.grphql;

import aydarss.fork.niffler.ayapi.GatewayGqlApiClient;
import aydarss.fork.niffler.ayconfig.Config;
import aydarss.fork.niffler.ayjupiter.ayannotation.aymeta.MyGqlTest;
import aydarss.fork.niffler.ayjupiter.ayextension.MyApiLoginExtension;
import org.junit.jupiter.api.extension.RegisterExtension;


@MyGqlTest
public abstract class BaseGraphQLTest {

  @RegisterExtension
  protected final MyApiLoginExtension apiLoginExtension = new MyApiLoginExtension(false);

  protected static final Config CFG = Config.getInstance();

  protected final GatewayGqlApiClient gatewayGqlApiClient = new GatewayGqlApiClient();

}
