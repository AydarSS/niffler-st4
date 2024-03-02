package aydarss.fork.niffler.aytest.grpc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.protobuf.Timestamp;
import guru.qa.grpc.niffler.grpc.CurrencyValues;
import guru.qa.grpc.niffler.grpc.Spend;
import guru.qa.grpc.niffler.grpc.SpendsRequest;
import guru.qa.grpc.niffler.grpc.SpendsResponse;
import guru.qa.grpc.niffler.grpc.StatisticRequest;
import guru.qa.grpc.niffler.grpc.StatisticResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SpendGrpcTest extends BaseSpendGrpcTest {

  @DisplayName("Проверим добавление траты")
  @Test
  void addSpendTest() {
    Spend candidate = Spend.newBuilder()
        .setSpendDate(Timestamp.newBuilder().build())
        .setAmount(123d)
        .setCategory("Обучение")
        .setCurrency(CurrencyValues.RUB)
        .setDescription("Покупка курса")
        .setUsername("rabbit")
        .build();

    Spend saved = blockingStubSpend.addSpend(candidate);

    Assertions.assertAll(
        () -> assertTrue(!saved.getId().equals("")),
        () -> assertEquals(123d, saved.getAmount()),
        () -> assertEquals("Обучение", saved.getCategory()),
        () -> assertEquals("Покупка курса", saved.getDescription()),
        () -> assertEquals("rabbit", saved.getUsername()),
        () -> assertEquals(CurrencyValues.RUB, saved.getCurrency())
    );
  }

  @DisplayName("Проверим изменение траты")
  @Test
  void editSpendTest() {
    Spend candidate = Spend.newBuilder()
        .setId("0db184f0-96d8-428c-822f-67f6a8d97011")
        .setSpendDate(Timestamp.newBuilder().build())
        .setAmount(123d)
        .setCategory("Обучение")
        .setCurrency(CurrencyValues.RUB)
        .setDescription("Покупка курса qaguru")
        .setUsername("rabbit")
        .build();

    Spend edited = blockingStubSpend.addSpend(candidate);

    Assertions.assertAll(
        () -> assertTrue(!edited.getId().equals("")),
        () -> assertEquals(123d, edited.getAmount()),
        () -> assertEquals("Обучение", edited.getCategory()),
        () -> assertEquals("Покупка курса qaguru", edited.getDescription()),
        () -> assertEquals("rabbit", edited.getUsername()),
        () -> assertEquals(CurrencyValues.RUB, edited.getCurrency())
    );

  }

  @DisplayName("Проверим получение списка трат")
  @Test
  void getSpendsTest() {
    SpendsRequest spendsRequest = SpendsRequest.newBuilder()
        .setUsername("rabbit")
        .setCurrency(CurrencyValues.RUB)
        .build();

    SpendsResponse spends = blockingStubSpend.getSpends(spendsRequest);

    spends.getAllSpendsList().size();

    assertTrue(spends.getAllSpendsList().stream()
        .anyMatch(spend -> spend.getAmount() == 123 && spend.getDescription()
            .equals("Покупка курса qaguru")));
  }

  @DisplayName("Проверим получение статистики")
  @Test
  void getStatisticsTest() {
    StatisticRequest request = StatisticRequest.newBuilder()
        .setUsername("rabbit")
        .setCurrency(CurrencyValues.RUB)
        .build();

    StatisticResponse statistic = blockingStubSpend.getStatistic(request);

    assertTrue(statistic
        .getStatisticsList()
        .get(0)
        .getStatisticByCategoryList().stream()
        .anyMatch(statisticByCategory ->
            statisticByCategory.getCategory().equals("Наука")
                && statisticByCategory.getTotal() == 58000));
  }

}
