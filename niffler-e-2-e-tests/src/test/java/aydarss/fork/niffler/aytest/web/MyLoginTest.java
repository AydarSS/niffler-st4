package aydarss.fork.niffler.aytest.web;

import aydarss.fork.niffler.aydb.aymodel.UserAuthEntity;
import aydarss.fork.niffler.ayjupiter.DbUserCRUDExtension;
import aydarss.fork.niffler.ayjupiter.MyTestUser;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DbUserCRUDExtension.class)
public class MyLoginTest extends BaseWebTest {


  @Test
  @MyTestUser(username = "Ivan", password = "12345")
  @DisplayName("Стастистика должна быть видна после авторизации")
  void statisticShouldBeVisibleAfterLoginWithDbUser(UserAuthEntity userAuth) {
    Selenide.open("http://127.0.0.1:3000/main");

    welcomePage
        .clickLoginBtn();

    loginPage
        .setLogin(userAuth.getUsername())
        .setPassword(userAuth.getPassword())
        .submit();

    mainPage
        .checkThatStatisticDisplayed();

  }

  @Test
  @MyTestUser
  @DisplayName("Стастистика должна быть видна после авторизации с пустым пользователем")
  void statisticShouldBeVisibleAfterLoginWithEmptyDbUser(UserAuthEntity userAuth) {
    Selenide.open("http://127.0.0.1:3000/main");
    welcomePage
        .clickLoginBtn();

    loginPage
        .setLogin(userAuth.getUsername())
        .setPassword(userAuth.getPassword())
        .submit();

    mainPage
        .checkThatStatisticDisplayed();
  }

}
