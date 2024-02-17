package aydarss.fork.niffler.aytest;

import static aydarss.fork.niffler.aypage.aymessage.SuccessMsg.PROFILE_MSG;

import aydarss.fork.niffler.aypage.LoginPage;
import aydarss.fork.niffler.aypage.MainPage;
import aydarss.fork.niffler.aypage.ProfilePage;
import aydarss.fork.niffler.aypage.WelcomePage;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;

public class ProfileTest extends BaseWebTest {

  @Test
  void testProfileUpdatedMsgShoulBeVisible() {
    Selenide.open("http://127.0.0.1:3000/main");
    new WelcomePage()
        .clickLoginBtn();

    new LoginPage()
        .setLogin("duck")
        .setPassword("12345")
        .submit();

    new MainPage()
        .getHeader()
        .goToProfilePage();

    new ProfilePage()
        .setName("Vasya")
        .setSurname("Ivanov")
        .submitName()
        .checkMessage(PROFILE_MSG);

  }

  @Test
  void testCategorShoulBeVisible() {
    Selenide.open("http://127.0.0.1:3000/main");
    new WelcomePage()
        .clickLoginBtn();

    new LoginPage()
        .setLogin("duck")
        .setPassword("12345")
        .submit();

    new MainPage()
        .getHeader()
        .goToProfilePage();

    new ProfilePage()
        .categoriesListContans("Обучение");

  }

}
