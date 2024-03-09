package aydarss.fork.niffler.aytest.web;

import static aydarss.fork.niffler.ayjupiter.ayannotation.MyUserQueue.UserType.INVITATION_SEND;
import static aydarss.fork.niffler.ayjupiter.ayannotation.MyUserQueue.UserType.WITH_FRIENDS;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import aydarss.fork.niffler.ayjupiter.ayannotation.MyUserQueue;
import aydarss.fork.niffler.ayjupiter.ayextension.UsersQueueExtension;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.model.UserJson;
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
  void invitationTableShouldNotBeEmpty(@MyUserQueue(INVITATION_SEND) UserJson user) {
    loginPage.loginByUserAndPassword(user.username(), user.testData().password());
    mainPage
        .checkThatStatisticDisplayed()
        .getHeader()
        .goToPeoplePage();
    allPeoplePage.findRecordInFriendsTable(user.testData().friendName());
  }

  @DisplayName("Проверим, что есть запись о друзьях, ожидающая активации")
  @Test
  void invitationTableShouldHaveRecordAboutFriends(@MyUserQueue(INVITATION_SEND) UserJson user) {
    loginPage.loginByUserAndPassword(user.username(), user.testData().password());
    mainPage
        .checkThatStatisticDisplayed()
        .getHeader()
        .goToPeoplePage();
    allPeoplePage.checkPendingInvitation(user.testData().friendName());
  }

  @DisplayName("Проверим, что есть аватар")
  @Test
  void invitationTableShouldHaveAvatar(@MyUserQueue(INVITATION_SEND) UserJson user,
      @MyUserQueue(WITH_FRIENDS) UserJson withFriendsUser) {
    assertAll("Проверка, что пользователь с друзьями выбрался или duck или dima",
        () -> assertTrue(
            withFriendsUser.username().equals("duck") || withFriendsUser.username().equals("dima"))
    );
    loginPage.loginByUserAndPassword(user.username(), user.testData().password());
    mainPage
        .checkThatStatisticDisplayed()
        .getHeader()
        .goToPeoplePage();
    allPeoplePage.checkAvatar(user.testData().friendName());
  }

}
