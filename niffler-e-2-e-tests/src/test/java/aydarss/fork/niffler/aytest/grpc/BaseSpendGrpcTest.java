package aydarss.fork.niffler.aytest.grpc;

import guru.qa.grpc.niffler.grpc.NifflerCategoryServiceGrpc;
import guru.qa.grpc.niffler.grpc.NifflerCurrencyServiceGrpc;
import guru.qa.grpc.niffler.grpc.NifflerSpendServiceGrpc;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.GrpcTest;
import guru.qa.niffler.utils.GrpcConsoleInterceptor;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.qameta.allure.grpc.AllureGrpc;

@GrpcTest
public abstract class BaseSpendGrpcTest {

  protected static final Config CFG = Config.getInstance();

  protected static Channel channel;
  protected static NifflerCategoryServiceGrpc.NifflerCategoryServiceBlockingStub blockingStubCategory;
  protected static NifflerSpendServiceGrpc.NifflerSpendServiceBlockingStub blockingStubSpend;


  static {
    channel = ManagedChannelBuilder.forAddress(CFG.spendGrpcHost(), CFG.spenGrpcPort())
        .intercept(new AllureGrpc(), new GrpcConsoleInterceptor())
        .usePlaintext()
        .build();

    blockingStubCategory = NifflerCategoryServiceGrpc.newBlockingStub(channel);
    blockingStubSpend = NifflerSpendServiceGrpc.newBlockingStub(channel);
  }
}
