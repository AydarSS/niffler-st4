package guru.qa.niffler.test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.jupiter.MyDbUser;
import guru.qa.niffler.jupiter.DbUserCRUDExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DbUserCRUDExtension.class)
public class MyLoginTest extends BaseWebTest {


  @Test
  @MyDbUser(username = "Ivan", password = "12345")
  void statisticShouldBeVisibleAfterLoginWithDbUser(UserAuthEntity userAuth) {
    Selenide.open("http://127.0.0.1:3000/main");
    new WelcomePage()
        .clickLoginBtn();

    new LoginPage()
        .setLogin(userAuth.getUsername())
        .setPassword(userAuth.getPassword())
        .submit();

    new MainPage()
        .checkThatStatisticDisplayed();

  }

  @Test
  @MyDbUser
  void statisticShouldBeVisibleAfterLoginWithEmptyDbUser(UserAuthEntity userAuth) {
    Selenide.open("http://127.0.0.1:3000/main");
    new WelcomePage()
        .clickLoginBtn();

    new LoginPage()
        .setLogin(userAuth.getUsername())
        .setPassword(userAuth.getPassword())
        .submit();

    new MainPage()
        .checkThatStatisticDisplayed();
  }

}
