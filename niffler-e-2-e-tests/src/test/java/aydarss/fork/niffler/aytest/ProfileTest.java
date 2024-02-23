package aydarss.fork.niffler.aytest;

import static aydarss.fork.niffler.aypage.aymessage.SuccessMsg.PROFILE_MSG;

import aydarss.fork.niffler.ayjupiter.DbUserCRUDExtension;
import aydarss.fork.niffler.ayjupiter.MyDbUser;
import aydarss.fork.niffler.ayjupiter.ayannotation.ApiLogin;
import aydarss.fork.niffler.ayjupiter.ayextension.ApiLoginExtension;
import aydarss.fork.niffler.ayjupiter.ayextension.ContextHolderExtension;
import aydarss.fork.niffler.aypage.ProfilePage;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({ContextHolderExtension.class, DbUserCRUDExtension.class, ApiLoginExtension.class})
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


  @Test
  @ApiLogin(user = @MyDbUser(username = "Ivan", password = "12345"))
  @DisplayName(
      "Сообщение об обновлении профиля пользователя должно показываться после его изменения"
          + "Авторизация через APi")
  void testApiLoginProfileUpdatedMsgShoulBeVisible() {
    Selenide.open(ProfilePage.URL);

    profilePage
        .setName("Vasya")
        .setSurname("Ivanov")
        .submitName()
        .checkMessage(PROFILE_MSG);

  }

  @Test
  @ApiLogin(user = @MyDbUser())
  @DisplayName(
      "Сообщение об обновлении профиля пользователя должно показываться после его изменения"
          + "Авторизация через APi. Рандомный юзер")
  void testApiLoginWithRandomUserProfileUpdatedMsgShoulBeVisible() {
    Selenide.open(ProfilePage.URL);

    profilePage
        .setName("Vasya")
        .setSurname("Ivanov")
        .submitName()
        .checkMessage(PROFILE_MSG);

  }

}
