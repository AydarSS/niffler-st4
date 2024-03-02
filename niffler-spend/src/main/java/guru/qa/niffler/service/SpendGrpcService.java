package guru.qa.niffler.service;

import static guru.qa.grpc.niffler.grpc.CurrencyValues.UNRECOGNIZED;

import com.google.protobuf.Timestamp;
import guru.qa.grpc.niffler.grpc.NifflerSpendServiceGrpc;
import guru.qa.grpc.niffler.grpc.Spend;
import guru.qa.grpc.niffler.grpc.SpendsRequest;
import guru.qa.grpc.niffler.grpc.SpendsResponse;
import guru.qa.grpc.niffler.grpc.Statistic;
import guru.qa.grpc.niffler.grpc.StatisticByCategory;
import guru.qa.grpc.niffler.grpc.StatisticRequest;
import guru.qa.grpc.niffler.grpc.StatisticResponse;
import guru.qa.niffler.data.CategoryEntity;
import guru.qa.niffler.data.SpendEntity;
import guru.qa.niffler.data.repository.CategoryRepository;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.StatisticByCategoryJson;
import guru.qa.niffler.model.StatisticJson;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class SpendGrpcService extends NifflerSpendServiceGrpc.NifflerSpendServiceImplBase {

  private final SpendRepository spendRepository;
  private final CategoryRepository categoryRepository;
  private final GrpcCurrencyClient grpcCurrencyClient;

  @Autowired
  public SpendGrpcService(SpendRepository spendRepository,
      CategoryRepository categoryRepository,
      GrpcCurrencyClient grpcCurrencyClient) {
    this.spendRepository = spendRepository;
    this.categoryRepository = categoryRepository;
    this.grpcCurrencyClient = grpcCurrencyClient;
  }

  @Override
  public void addSpend(Spend request, StreamObserver<Spend> responseObserver) {
    final String username = request.getUsername();
    final String category = request.getCategory();

    SpendEntity spendEntity = new SpendEntity();
    spendEntity.setUsername(username);
    spendEntity.setSpendDate(toDate(request.getSpendDate()));
    spendEntity.setCurrency(CurrencyValues.valueOf(request.getCurrency().name()));
    spendEntity.setDescription(request.getDescription());
    spendEntity.setAmount(request.getAmount());

    Optional<CategoryEntity> categoryEntity = categoryRepository.findAllByUsername(username)
        .stream()
        .filter(c -> c.getCategory().equals(category))
        .findFirst();
    if (!categoryEntity.isPresent()) {
      responseObserver.onError(
          new RuntimeException("Can`t find category by given name: " + category));
    }

    spendEntity.setCategory(categoryEntity.get());
    SpendEntity saved = spendRepository.save(spendEntity);
    responseObserver.onNext(Spend.newBuilder()
        .setId(saved.getId().toString())
        .setSpendDate(fromDate(saved.getSpendDate()))
        .setCategory(saved.getCategory().getCategory())
        .setCurrency(guru.qa.grpc.niffler.grpc.CurrencyValues.valueOf(saved.getCurrency().name()))
        .setAmount(saved.getAmount())
        .setDescription(saved.getDescription())
        .setUsername(saved.getUsername())
        .build());
    responseObserver.onCompleted();
  }

  @Override
  public void editSpend(Spend request, StreamObserver<Spend> responseObserver) {
    Optional<SpendEntity> spendById = spendRepository.findById(UUID.fromString(request.getId()));
    if (spendById.isEmpty()) {
      responseObserver.onError(
          new RuntimeException("Can`t find spend by given id: " + request.getId()));
    } else {
      final String category = request.getCategory();
      Optional<CategoryEntity> categoryEntity = categoryRepository.findAllByUsername(
              request.getUsername())
          .stream()
          .filter(c -> c.getCategory().equals(category))
          .findFirst();

      if (!categoryEntity.isPresent()) {
        responseObserver.onError(
            new RuntimeException("Can`t find category by given name: " + category));
      }

      SpendEntity spendEntity = spendById.get();
      spendEntity.setSpendDate(toDate(request.getSpendDate()));
      spendEntity.setCategory(categoryEntity.get());
      spendEntity.setAmount(request.getAmount());
      spendEntity.setDescription(request.getDescription());

      SpendEntity saved = spendRepository.save(spendEntity);

      responseObserver.onNext(Spend.newBuilder()
          .setId(saved.getId().toString())
          .setSpendDate(fromDate(saved.getSpendDate()))
          .setCategory(saved.getCategory().getCategory())
          .setCurrency(guru.qa.grpc.niffler.grpc.CurrencyValues.valueOf(saved.getCurrency().name()))
          .setAmount(saved.getAmount())
          .setDescription(saved.getDescription())
          .setUsername(saved.getUsername())
          .build());
      responseObserver.onCompleted();
    }
  }

  @Override
  public void getSpends(SpendsRequest request, StreamObserver<SpendsResponse> responseObserver) {
    final String username = request.getUsername();
    final CurrencyValues filterCurrency = request.getCurrency().equals(UNRECOGNIZED) ? null
        : CurrencyValues.valueOf(request.getCurrency().name());
    final Date dateFrom = request.hasFrom() ? toDate(request.getFrom()) : null;
    final Date dateTo = request.hasTo() ? toDate(request.getTo()) : null;

    List<Spend> spends = getSpendsEntityForUser(username, filterCurrency, dateFrom, dateTo)
        .map(spend -> Spend.newBuilder()
            .setId(spend.getId().toString())
            .setSpendDate(fromDate(spend.getSpendDate()))
            .setCategory(spend.getCategory().getCategory())
            .setAmount(spend.getAmount())
            .setDescription(spend.getDescription())
            .setUsername(spend.getUsername())
            .build())
        .toList();

    responseObserver.onNext(SpendsResponse.newBuilder()
        .addAllAllSpends(spends)
        .build());
    responseObserver.onCompleted();
  }

 /* @Override
  public void deleteSpend(DeleteSpendsRequest request, StreamObserver<Empty> responseObserver) {
    final String username = request.getUsername();
    List<UUID> ids = request.getIdsList().stream().map(id -> UUID.fromString(id)).toList();
    spendRepository.deleteByUsernameAndIdIn(username, ids);
    responseObserver.onNext(Empty.newBuilder().getDefaultInstanceForType());
    responseObserver.onCompleted();
  }*/

  @Override
  public void getStatistic(StatisticRequest request,
      StreamObserver<StatisticResponse> responseObserver) {
    final String username = request.getUsername();
    final CurrencyValues filterCurrency = request.getCurrency().equals(UNRECOGNIZED) ? null
        : CurrencyValues.valueOf(request.getCurrency().name());
    final Date dateFrom = request.hasFrom() ? toDate(request.getFrom()) : null;
    final Date dateTo = request.hasTo() ? toDate(request.getTo()) : null;

    List<SpendEntity> spendEntities = getSpendsEntityForUser(username, filterCurrency, dateFrom,
        dateTo).toList();
    List<StatisticJson> result = new ArrayList<>();

    CurrencyValues[] desiredCurrenciesInResponse = resolveDesiredCurrenciesInStatistic(
        filterCurrency);

    for (CurrencyValues statisticCurrency : desiredCurrenciesInResponse) {
      StatisticJson enriched = calculateStatistic(statisticCurrency, username,
          CurrencyValues.valueOf(request.getCurrency().name()), spendEntities, dateTo);
      result.add(enriched);
    }
    responseObserver.onNext(
        StatisticResponse.newBuilder().addAllStatistics(fromJsonToProto(result)).build());
    responseObserver.onCompleted();
  }

  private List<Statistic> fromJsonToProto(List<StatisticJson> statisticJsons) {
    return statisticJsons.stream().map(statisticJson -> {
      List<StatisticByCategory> statisticByCategories =
          statisticJson.categoryStatistics()
              .stream()
              .map(statisticByCategoryJson -> {
                List<Spend> spends = statisticByCategoryJson.spends().stream()
                    .map(spend -> Spend.newBuilder()
                        .setId(spend.id().toString())
                        .setSpendDate(fromDate(spend.spendDate()))
                        .setCategory(spend.category())
                        .setCurrency(guru.qa.grpc.niffler.grpc.CurrencyValues.valueOf(
                            spend.currency().name()))
                        .setAmount(spend.amount())
                        .setDescription(spend.description())
                        .setUsername(spend.username())
                        .build())
                    .toList();

                return StatisticByCategory.newBuilder()
                    .setCategory(statisticByCategoryJson.category())
                    .setTotal(statisticByCategoryJson.total())
                    .setTotalInUserDefaultCurrency(
                        statisticByCategoryJson.totalInUserDefaultCurrency())
                    .addAllSpends(spends)
                    .build();
              }).toList();

      return Statistic.newBuilder()
          .setDateFrom(Objects.isNull(statisticJson.dateFrom()) ? Timestamp.newBuilder().build()
              : fromDate(statisticJson.dateFrom()))
          .setDateTo(Objects.isNull(statisticJson.dateTo()) ? Timestamp.newBuilder().build()
              : fromDate(statisticJson.dateTo()))
          .setCurrency(
              guru.qa.grpc.niffler.grpc.CurrencyValues.valueOf(statisticJson.currency().name()))
          .setTotal(statisticJson.total())
          .setUserDefaultCurrency(guru.qa.grpc.niffler.grpc.CurrencyValues.valueOf(
              statisticJson.userDefaultCurrency().name()))
          .addAllStatisticByCategory(statisticByCategories)
          .build();
    }).toList();
  }

  CurrencyValues[] resolveDesiredCurrenciesInStatistic(@Nullable CurrencyValues filterCurrency) {
    return filterCurrency != null
        ? new CurrencyValues[]{filterCurrency}
        : CurrencyValues.values();
  }

  StatisticJson calculateStatistic(@Nonnull CurrencyValues statisticCurrency,
      @Nonnull String username,
      @Nonnull CurrencyValues userCurrency,
      @Nonnull List<SpendEntity> spendEntities,
      @Nullable Date dateTo) {
    StatisticJson statistic = createDefaultStatisticJson(statisticCurrency, userCurrency, dateTo);
    List<SpendEntity> sortedSpends = spendEntities.stream()
        .filter(se -> se.getCurrency() == statisticCurrency)
        .sorted(Comparator.comparing(SpendEntity::getSpendDate))
        .toList();

    statistic = calculateStatistic(statistic, statisticCurrency, userCurrency, sortedSpends);
    Map<String, List<SpendJson>> spendsByCategory = bindSpendsToCategories(sortedSpends);

    List<StatisticByCategoryJson> sbcjResult = new ArrayList<>();
    for (Map.Entry<String, List<SpendJson>> entry : spendsByCategory.entrySet()) {
      double total = entry.getValue().stream()
          .map(SpendJson::amount)
          .map(BigDecimal::valueOf)
          .reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();

      double totalInUserDefaultCurrency = grpcCurrencyClient.calculate(
          total,
          statisticCurrency,
          userCurrency
      ).doubleValue();

      sbcjResult.add(new StatisticByCategoryJson(
          entry.getKey(),
          total,
          totalInUserDefaultCurrency,
          entry.getValue()
      ));
    }

    categoryRepository.findAllByUsername(username).stream()
        .filter(c -> !spendsByCategory.containsKey(c.getCategory()))
        .map(c -> new StatisticByCategoryJson(
            c.getCategory(),
            0.0,
            0.0,
            Collections.emptyList()
        ))
        .forEach(sbcjResult::add);

    sbcjResult.sort(Comparator.comparing(StatisticByCategoryJson::category));
    statistic.categoryStatistics().addAll(sbcjResult);
    return statistic;
  }

  StatisticJson calculateStatistic(@Nonnull StatisticJson statistic,
      @Nonnull CurrencyValues statisticCurrency,
      @Nonnull CurrencyValues userCurrency,
      @Nonnull List<SpendEntity> sortedSpends) {
    StatisticJson enrichedStatistic = statistic;
    for (SpendEntity spend : sortedSpends) {
      enrichedStatistic =
          enrichStatisticTotalInUserCurrencyByAllStreamElements(
              enrichStatisticTotalAmountByAllStreamElements(
                  enrichStatisticDateFromByFirstStreamElement(
                      enrichedStatistic
                  ).apply(spend)
              ).apply(spend), statisticCurrency, userCurrency)
              .apply(spend);
    }
    return enrichedStatistic;
  }

  Function<SpendEntity, StatisticJson> enrichStatisticDateFromByFirstStreamElement(
      @Nonnull StatisticJson statistic) {
    return se -> {
      if (statistic.dateFrom() == null) {
        return new StatisticJson(
            se.getSpendDate(),
            statistic.dateTo(),
            statistic.currency(),
            statistic.total(),
            statistic.userDefaultCurrency(),
            statistic.totalInUserDefaultCurrency(),
            statistic.categoryStatistics()
        );
      } else {
        return statistic;
      }
    };
  }

  @Nonnull
  Function<SpendEntity, StatisticJson> enrichStatisticTotalAmountByAllStreamElements(
      @Nonnull StatisticJson statistic) {
    return se -> new StatisticJson(
        statistic.dateFrom(),
        statistic.dateTo(),
        statistic.currency(),
        BigDecimal.valueOf(statistic.total())
            .add(BigDecimal.valueOf(se.getAmount()))
            .doubleValue(),
        statistic.userDefaultCurrency(),
        statistic.totalInUserDefaultCurrency(),
        statistic.categoryStatistics()
    );
  }

  @Nonnull
  Function<SpendEntity, StatisticJson> enrichStatisticTotalInUserCurrencyByAllStreamElements(
      @Nonnull StatisticJson statistic,
      @Nonnull CurrencyValues statisticCurrency,
      @Nonnull CurrencyValues userCurrency) {
    return se -> new StatisticJson(
        statistic.dateFrom(),
        statistic.dateTo(),
        statistic.currency(),
        statistic.total(),
        statistic.userDefaultCurrency(),
        (userCurrency != statisticCurrency)
            ? BigDecimal.valueOf(statistic.totalInUserDefaultCurrency()).add(
            grpcCurrencyClient.calculate(se.getAmount(), se.getCurrency(), userCurrency)
        ).doubleValue()
            : statistic.total(),
        statistic.categoryStatistics()
    );
  }

  StatisticJson createDefaultStatisticJson(@Nonnull CurrencyValues statisticCurrency,
      @Nonnull CurrencyValues userCurrency,
      @Nullable Date dateTo) {
    return new StatisticJson(
        null,
        dateTo,
        statisticCurrency,
        0.0,
        userCurrency,
        0.0,
        new ArrayList<>()
    );
  }

  Map<String, List<SpendJson>> bindSpendsToCategories(@Nonnull List<SpendEntity> sortedSpends) {
    return sortedSpends.stream().map(SpendJson::fromEntity)
        .collect(Collectors.groupingBy(
            SpendJson::category,
            HashMap::new,
            Collectors.toCollection(ArrayList::new)
        ));
  }

  private Stream<SpendEntity> getSpendsEntityForUser(@Nonnull String username,
      @Nullable CurrencyValues filterCurrency,
      @Nullable Date dateFrom,
      @Nullable Date dateTo) {

    dateTo = dateTo == null
        ? new Date()
        : dateTo;

    List<SpendEntity> spends = dateFrom == null
        ? spendRepository.findAllByUsernameAndSpendDateLessThanEqual(username, dateTo)
        : spendRepository.findAllByUsernameAndSpendDateGreaterThanEqualAndSpendDateLessThanEqual(
            username, dateFrom, dateTo);

    return spends.stream()
        .filter(se -> filterCurrency == null || se.getCurrency() == filterCurrency);
  }

  private Date toDate(Timestamp ts) {
    return new Date(ts.getSeconds() * 1000);
  }

  private Timestamp fromDate(Date date) {
    LocalDate localDate = date.toInstant().atZone(ZoneOffset.UTC).toLocalDate();
    Instant instant = localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
    return Timestamp.newBuilder()
        .setSeconds(instant.getEpochSecond())
        .setNanos(instant.getNano())
        .build();
  }
}
