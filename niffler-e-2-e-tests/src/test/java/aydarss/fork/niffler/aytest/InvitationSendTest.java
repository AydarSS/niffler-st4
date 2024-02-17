package aydarss.fork.niffler.aytest;

import static aydarss.fork.niffler.ayjupiter.ayannotation.User.UserType.INVITATION_SEND;
import static aydarss.fork.niffler.ayjupiter.ayannotation.User.UserType.WITH_FRIENDS;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import aydarss.fork.niffler.ayjupiter.ayannotation.User;
import aydarss.fork.niffler.ayjupiter.ayextension.UsersQueueExtension;
import aydarss.fork.niffler.aymodel.UserJson;
import aydarss.fork.niffler.aypage.AllPeoplePage;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UsersQueueExtension.class)
public class InvitationSendTest extends BaseWebTest {

  @BeforeEach
  void doLogin() {
    Selenide.open("http://127.0.0.1:3000/main");
    welcomePage.clickLoginBtn();
  }

  @DisplayName("Проверим, что есть отправленная заявка в друзья и имя совпадает с ожидаемым")
  @Test
  void invitationTableShouldNotBeEmpty(@User(INVITATION_SEND) UserJson user) {
    loginPage.loginByUserAndPassword(user.username(), user.testData().password());
    mainPage
        .checkThatStatisticDisplayed()
        .clickAllPeopleBtn();
    new AllPeoplePage().findRecordInFriendsTable(user.testData().friendName());
  }

  @DisplayName("Проверим, что есть запись о друзьях, ожидающая активации")
  @Test
  void invitationTableShouldHaveRecordAboutFriends(@User(INVITATION_SEND) UserJson user) {
    loginPage.loginByUserAndPassword(user.username(), user.testData().password());
    mainPage
        .checkThatStatisticDisplayed()
        .clickAllPeopleBtn();
    new AllPeoplePage().checkPendingInvitation(user.testData().friendName());
  }

  @DisplayName("Проверим, что есть аватар")
  @Test
  void invitationTableShouldHaveAvatar(@User(INVITATION_SEND) UserJson user,
      @User(WITH_FRIENDS) UserJson withFriendsUser) {
    assertAll("Проверка, что пользователь с друзьями выбрался или duck или dima",
        () -> assertTrue(
            withFriendsUser.username().equals("duck") || withFriendsUser.username().equals("dima"))
    );
    loginPage.loginByUserAndPassword(user.username(), user.testData().password());
    mainPage
        .checkThatStatisticDisplayed()
        .clickAllPeopleBtn();
    new AllPeoplePage().checkAvatar(user.testData().friendName());
  }

}
