package aydarss.fork.niffler.aytest;

import aydarss.fork.niffler.aydb.aymodel.UserAuthEntity;
import aydarss.fork.niffler.ayjupiter.DbUserCRUDExtension;
import aydarss.fork.niffler.ayjupiter.MyDbUser;
import aydarss.fork.niffler.aypage.LoginPage;
import aydarss.fork.niffler.aypage.MainPage;
import aydarss.fork.niffler.aypage.WelcomePage;
import com.codeborne.selenide.Selenide;
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
