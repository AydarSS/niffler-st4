package aydarss.fork.niffler.aytest;

import static aydarss.fork.niffler.aypage.aymessage.SuccessMsg.PROFILE_MSG;

import aydarss.fork.niffler.aypage.LoginPage;
import aydarss.fork.niffler.aypage.MainPage;
import aydarss.fork.niffler.aypage.ProfilePage;
import aydarss.fork.niffler.aypage.WelcomePage;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProfileTest extends BaseWebTest {

  @Test
  @DisplayName("Сообщение об обновлении профиля пользователя должно показываться после его изменения")
  void testProfileUpdatedMsgShoulBeVisible() {
    Selenide.open("http://127.0.0.1:3000/main");
    welcomePage
        .clickLoginBtn();

    loginPage
        .setLogin("duck")
        .setPassword("12345")
        .submit();

    loginPage
        .getHeader()
        .goToProfilePage();

    profilePage
        .setName("Vasya")
        .setSurname("Ivanov")
        .submitName()
        .checkMessage(PROFILE_MSG);

  }

  @Test
  @DisplayName("Список категорий пользователя должен содержать Обучение")
  void testCategorShoulBeVisible() {
    Selenide.open("http://127.0.0.1:3000/main");
    welcomePage
        .clickLoginBtn();

    loginPage
        .setLogin("duck")
        .setPassword("12345")
        .submit();

    mainPage
        .getHeader()
        .goToProfilePage();

    profilePage
        .categoriesListContans("Обучение");

  }

}
