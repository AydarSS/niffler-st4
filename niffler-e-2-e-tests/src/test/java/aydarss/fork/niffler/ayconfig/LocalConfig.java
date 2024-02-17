package aydarss.fork.niffler.ayconfig;

public class LocalConfig implements Config {

  static final LocalConfig instance = new LocalConfig();

  private LocalConfig() {
  }

  @Override
  public String frontUrl() {
    return "http://127.0.0.1:3000";
  }

  @Override
  public String jdbcHost() {
    return "localhost";
  }

  @Override
  public String spendUrl() {
    return "http://127.0.0.1:8093";
  }

  @Override
  public String userUrl() {
    return "http://127.0.0.1:8089";
  }

  @Override
  public String currencyUrl() {
    return "http://127.0.0.1:8091";
  }

}
