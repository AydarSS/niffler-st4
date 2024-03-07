package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    return "docker".equals(System.getProperty("test.env"))
        ? DockerConfig.instance
        : LocalConfig.instance;
  }

  String frontUrl();

  String authUrl();

  String gatewayUrl();

  String jdbcHost();

  String spendUrl();

  String userUrl();

  String currencyUrl();

  String currencyGrpcHost();

  default String jdbcUser() {
    return "postgres";
  }

  default String jdbcPassword() {
    return "secret";
  }

  default int jdbcPort() {
    return 5432;
  }

  default int currencyGrpcPort() {
    return 8092;
  }

  String spendGrpcHost();

  default int spenGrpcPort() {
    return 8099;
  }
}
