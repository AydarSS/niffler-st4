package aydarss.fork.niffler.ayapi.ayinterceptor;


import aydarss.fork.niffler.ayjupiter.ayextension.ApiLoginExtension;
import aydarss.fork.niffler.ayjupiter.ayextension.ContextHolderExtension.Holder;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

public class CodeInterceptor implements Interceptor {
  @Override
  public Response intercept(Chain chain) throws IOException {
    final Response response = chain.proceed(chain.request());
    if (response.isRedirect()) {
      final String location = response.header("Location");
      if (location.contains("code=")) {
        final String code = StringUtils.substringAfter(location, "code=");
        ApiLoginExtension.setCode(
            Holder.INSTANCE.context(),
            code
        );
      }
    }
    return response;
  }
}
