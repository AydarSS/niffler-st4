package aydarss.fork.niffler.aydb;

import aydarss.fork.niffler.ayconfig.Config;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import org.postgresql.ds.PGSimpleDataSource;

public enum DataSourceProvider {
  INSTANCE;

  private static final Config cfg = Config.getInstance();

  private final Map<Database, DataSource> store = new ConcurrentHashMap<>();

  public DataSource dataSource(Database database) {
    return store.computeIfAbsent(database, k -> {
      PGSimpleDataSource ds = new PGSimpleDataSource();
      ds.setURL(k.getUrl());
      ds.setUser(cfg.jdbcUser());
      ds.setPassword(cfg.jdbcPassword());
      return ds;
    });
  }
}
