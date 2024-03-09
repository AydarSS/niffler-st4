package aydarss.fork.niffler.ayapi.ayauth;

import aydarss.fork.niffler.ayapi.RestClient;
import aydarss.fork.niffler.ayapi.aycookie.CookieInterceptor;
import aydarss.fork.niffler.ayapi.ayinterceptor.CodeInterceptor;
import aydarss.fork.niffler.ayapi.ayinterceptor.CodeInterceptorForMyApiLogin;
import aydarss.fork.niffler.ayjupiter.ayextension.MyApiLoginExtension;
import com.fasterxml.jackson.databind.JsonNode;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.junit.jupiter.api.extension.ExtensionContext;

public class AuthApiClientForMyApiLogin extends RestClient {

  private final AuthApi authApi;

  public AuthApiClientForMyApiLogin() {
    super(
        CFG.authUrl(),
        true,
        new CodeInterceptorForMyApiLogin(),
        CookieInterceptor.INSTANCE
    );
    authApi = retrofit.create(AuthApi.class);
  }

  public void doLogin(ExtensionContext context, String username, String password) throws Exception {
    authApi.authorize(
        "code",
        "client",
        "openid",
        CFG.frontUrl() + "/authorized",
        MyApiLoginExtension.getCodChallenge(context),
        "S256"
    ).execute();

    authApi.login(
        username,
        password,
        MyApiLoginExtension.getCsrfToken()
    ).execute();

    JsonNode responseBody = authApi.token(
        "Basic " + new String(
            Base64.getEncoder().encode("client:secret".getBytes(StandardCharsets.UTF_8))),
        "client",
        "http://127.0.0.1:3000/authorized",
        "authorization_code",
        MyApiLoginExtension.getCode(context),
        MyApiLoginExtension.getCodeVerifier(context)
    ).execute().body();

    final String token = responseBody.get("id_token").asText();
    MyApiLoginExtension.setToken(context, token);
  }
}
