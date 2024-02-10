package guru.qa.niffler.test;

import static guru.qa.niffler.page.message.SuccessMsg.PROFILE_MSG;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.page.WelcomePage;
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
