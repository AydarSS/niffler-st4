package aydarss.fork.niffler.ayjupiter.ayextension;

import aydarss.fork.niffler.ayapi.ayauth.AuthApiClient;
import aydarss.fork.niffler.ayapi.aycookie.CookieInterceptor;
import aydarss.fork.niffler.ayapi.aycookie.ThreadSafeCookieManager;
import aydarss.fork.niffler.ayconfig.Config;
import aydarss.fork.niffler.aydb.aymodel.UserAuthEntity;
import aydarss.fork.niffler.ayjupiter.DbUserCRUDExtension;
import aydarss.fork.niffler.ayjupiter.ayannotation.ApiLogin;
import aydarss.fork.niffler.utils.OauthUtils;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SessionStorage;
import com.codeborne.selenide.WebDriverRunner;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.openqa.selenium.Cookie;

public class ApiLoginExtension implements BeforeEachCallback, AfterTestExecutionCallback {

  private static final Config CFG = Config.getInstance();
  private final AuthApiClient authApiClient = new AuthApiClient();

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ApiLoginExtension.class);


  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    ApiLogin apiLogin = AnnotationSupport.findAnnotation(
        extensionContext.getRequiredTestMethod(),
        ApiLogin.class
    ).orElse(null);

    if (apiLogin != null) {
      final String codeVerifier = OauthUtils.generateCodeVerifier();
      final String codeChallenge = OauthUtils.generateCodeChallange(codeVerifier);
      setCodeVerifier(extensionContext, codeVerifier);
      setCodChallenge(extensionContext, codeChallenge);
      List<String> credentials = getCredentials(apiLogin,extensionContext);
      authApiClient.doLogin(extensionContext,credentials.get(0), credentials.get(1));

      Selenide.open(CFG.frontUrl());
      SessionStorage sessionStorage = Selenide.sessionStorage();
      sessionStorage.setItem(
          "codeChallenge", getCodChallenge(extensionContext)
      );
      sessionStorage.setItem(
          "id_token", getToken(extensionContext)
      );
      sessionStorage.setItem(
          "codeVerifier", getCodeVerifier(extensionContext)
      );

      WebDriverRunner.getWebDriver().manage().addCookie(
          jsessionCookie()
      );
      Selenide.refresh();
    }
  }

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
    ThreadSafeCookieManager.INSTANCE.removeAll();
  }

  public static void setCodeVerifier(ExtensionContext context, String codeVerifier) {
    context.getStore(ApiLoginExtension.NAMESPACE).put("code_verifier", codeVerifier);
  }

  public static void setCodChallenge(ExtensionContext context, String codeChallenge) {
    context.getStore(ApiLoginExtension.NAMESPACE).put("code_challenge", codeChallenge);
  }

  public static void setCode(ExtensionContext context, String code) {
    context.getStore(ApiLoginExtension.NAMESPACE).put("code", code);
  }

  public static void setToken(ExtensionContext context, String token) {
    context.getStore(ApiLoginExtension.NAMESPACE).put("token", token);
  }

  public static String getCodeVerifier(ExtensionContext context) {
    return context.getStore(ApiLoginExtension.NAMESPACE).get("code_verifier", String.class);
  }

  public static String getCodChallenge(ExtensionContext context) {
    return context.getStore(ApiLoginExtension.NAMESPACE).get("code_challenge", String.class);
  }

  public static String getCode(ExtensionContext context) {
    return context.getStore(ApiLoginExtension.NAMESPACE).get("code", String.class);
  }

  public static String getToken(ExtensionContext context) {
    return context.getStore(ApiLoginExtension.NAMESPACE).get("token", String.class);
  }

  public static String getCsrfToken() {
    return CookieInterceptor.INSTANCE.getCookie("XSRF-TOKEN");
  }

  public Cookie jsessionCookie() {
    return new Cookie(
        "JSESSIONID",
        CookieInterceptor.INSTANCE.getCookie("JSESSIONID")
    );
  }

  private List<String> getCredentials (ApiLogin apiLogin, ExtensionContext extensionContext) {
    List<String> listOfUsernameAndPassword = new ArrayList<>();
    if (apiLogin.user().fake()) {
      listOfUsernameAndPassword.add(apiLogin.username());
      listOfUsernameAndPassword.add(apiLogin.password());
      return listOfUsernameAndPassword;
    }
    Map userDataMap = extensionContext.getStore(DbUserCRUDExtension.NAMESPACE)
        .get(extensionContext.getUniqueId(), Map.class);
    UserAuthEntity userAuthEntity = (UserAuthEntity) userDataMap.get("userAuthDB");
    listOfUsernameAndPassword.add(userAuthEntity.getUsername());
    listOfUsernameAndPassword.add(userAuthEntity.getPassword());
    return listOfUsernameAndPassword;
  }
}
