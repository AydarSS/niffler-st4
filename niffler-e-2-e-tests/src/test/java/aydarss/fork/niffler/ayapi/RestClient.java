package aydarss.fork.niffler.ayapi;


import aydarss.fork.niffler.ayapi.aycookie.ThreadSafeCookieManager;
import aydarss.fork.niffler.ayconfig.Config;
import java.net.CookieManager;
import java.net.CookiePolicy;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public abstract class RestClient {

  protected static final Config CFG = Config.getInstance();

  protected final OkHttpClient okHttpClient;
  protected final Retrofit retrofit;

  public RestClient(@Nonnull String baseUri) {
    this(
        baseUri,
        false,
        JacksonConverterFactory.create(),
        null
    );
  }

  public RestClient(@Nonnull String baseUri, boolean followRedirect) {
    this(
        baseUri,
        followRedirect,
        JacksonConverterFactory.create(),
        null
    );
  }

  public RestClient(@Nonnull String baseUri, boolean followRedirect, @Nonnull Converter.Factory converter) {
    this(
        baseUri,
        followRedirect,
        converter,
        null
    );
  }

  public RestClient(@Nonnull String baseUri, boolean followRedirect, @Nullable Interceptor... interceptors) {
    this(
        baseUri,
        followRedirect,
        JacksonConverterFactory.create(),
        interceptors
    );
  }

  public RestClient(@Nonnull String baseUri,
                    boolean followRedirect,
                    @Nonnull Converter.Factory converter,
                    @Nullable Interceptor... interceptors) {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    builder.followRedirects(followRedirect);
    if (interceptors != null) {
      for (Interceptor interceptor : interceptors) {
        builder.addNetworkInterceptor(interceptor);
      }
    }
 //   builder.cookieJar(new JavaNetCookieJar(new CookieManager(ThreadSafeCookieManager.INSTANCE, CookiePolicy.ACCEPT_ALL)));
    builder.addNetworkInterceptor(
        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    );
    this.okHttpClient = builder.build();
    this.retrofit = new Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUri)
        .addConverterFactory(converter)
        .build();
  }
}
