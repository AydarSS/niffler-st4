package aydarss.fork.niffler.ayjupiter.ayextension;

import aydarss.fork.niffler.ayapi.ayauth.AuthApiClientForMyApiLogin;
import aydarss.fork.niffler.ayapi.aycookie.CookieInterceptor;
import aydarss.fork.niffler.ayapi.aycookie.ThreadSafeCookieManager;
import aydarss.fork.niffler.ayconfig.Config;
import aydarss.fork.niffler.ayjupiter.MyTestUser;
import aydarss.fork.niffler.ayjupiter.ayannotation.ApiLogin;
import aydarss.fork.niffler.ayjupiter.ayannotation.MyUser;
import aydarss.fork.niffler.ayjupiter.ayannotation.Token;
import aydarss.fork.niffler.utils.OauthUtils;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SessionStorage;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.model.UserJson;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;
import org.openqa.selenium.Cookie;

public class MyApiLoginExtension implements BeforeEachCallback, AfterTestExecutionCallback,
    ParameterResolver {

  private static final Config CFG = Config.getInstance();
  private final AuthApiClientForMyApiLogin authApiClient = new AuthApiClientForMyApiLogin();
  private final boolean initBrowser;

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
      MyApiLoginExtension.class);

  public MyApiLoginExtension() {
    this(true);
  }

  public MyApiLoginExtension(boolean initBrowser) {
    this.initBrowser = initBrowser;
  }

  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    ApiLogin apiLogin = AnnotationSupport.findAnnotation(
        extensionContext.getRequiredTestMethod(),
        ApiLogin.class
    ).orElse(null);

    final String login;
    final String password;

    if (apiLogin != null) {
      MyTestUser testUser = apiLogin.user();
      if (!testUser.fake()) {
        UserJson createdUserForApiLogin = getCreatedUserForApiLogin(extensionContext);
        login = createdUserForApiLogin.username();
        password = createdUserForApiLogin.testData().password();
      } else {
        login = apiLogin.username();
        password = apiLogin.password();
      }

      final String codeVerifier = OauthUtils.generateCodeVerifier();
      final String codeChallenge = OauthUtils.generateCodeChallange(codeVerifier);
      setCodeVerifier(extensionContext, codeVerifier);
      setCodChallenge(extensionContext, codeChallenge);
      authApiClient.doLogin(extensionContext, login, password);

      if (initBrowser) {
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
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return
        AnnotationSupport.findAnnotation(parameterContext.getParameter(), Token.class).isPresent()
            &&
            parameterContext.getParameter().getType().isAssignableFrom(String.class);
  }

  @Override
  public String resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return "Bearer " + getToken(extensionContext);
  }

  @SuppressWarnings("unchecked")
  private static UserJson getCreatedUserForApiLogin(ExtensionContext extensionContext) {
    return ((List<UserJson>) extensionContext.getStore(MyCreateUserExtension.CREATE_USER_NAMESPACE)
        .get(extensionContext.getUniqueId(), Map.class)
        .get(MyUser.Point.INNER))
        .get(0);
  }

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
    ThreadSafeCookieManager.INSTANCE.removeAll();
  }

  public static void setCodeVerifier(ExtensionContext context, String codeVerifier) {
    context.getStore(MyApiLoginExtension.NAMESPACE).put("code_verifier", codeVerifier);
  }

  public static void setCodChallenge(ExtensionContext context, String codeChallenge) {
    context.getStore(MyApiLoginExtension.NAMESPACE).put("code_challenge", codeChallenge);
  }

  public static void setCode(ExtensionContext context, String code) {
    context.getStore(MyApiLoginExtension.NAMESPACE).put("code", code);
  }

  public static void setToken(ExtensionContext context, String token) {
    context.getStore(MyApiLoginExtension.NAMESPACE).put("token", token);
  }

  public static String getCodeVerifier(ExtensionContext context) {
    return context.getStore(MyApiLoginExtension.NAMESPACE).get("code_verifier", String.class);
  }

  public static String getCodChallenge(ExtensionContext context) {
    return context.getStore(MyApiLoginExtension.NAMESPACE).get("code_challenge", String.class);
  }

  public static String getCode(ExtensionContext context) {
    return context.getStore(MyApiLoginExtension.NAMESPACE).get("code", String.class);
  }

  public static String getToken(ExtensionContext context) {
    return context.getStore(MyApiLoginExtension.NAMESPACE).get("token", String.class);
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
}
