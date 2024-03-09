package aydarss.fork.niffler.aytest.web;

import static aydarss.fork.niffler.ayjupiter.ayannotation.MyUserQueue.UserType.INVITATION_RECIEVED;

import aydarss.fork.niffler.ayjupiter.ayannotation.MyUserQueue;
import aydarss.fork.niffler.ayjupiter.ayextension.UsersQueueExtension;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UsersQueueExtension.class)
public class InvitationReceivedTest extends BaseWebTest {

  @BeforeEach
  void doLogin(@MyUserQueue(INVITATION_RECIEVED) UserJson user) {
    Selenide.open("http://127.0.0.1:3000/main");
    welcomePage.clickLoginBtn();
    loginPage.loginByUserAndPassword(user.username(), user.testData().password());
    mainPage
        .checkThatStatisticDisplayed();

    mainPage
        .getHeader()
        .goToPeoplePage();
  }

  @DisplayName("Проверим, что есть заявка в друзья и имя совпадает с ожидаемым")
  @Test
  void invitationTableShouldNotBeEmpty(@MyUserQueue(INVITATION_RECIEVED) UserJson user) {
    allPeoplePage.findRecordInFriendsTable(user.testData().friendName());
  }

  @DisplayName("Проверим, что есть кнопка принять запрос в друзья")
  @Test
  void invitationTableShouldHaveSubmitInvitationBtn(@MyUserQueue(INVITATION_RECIEVED) UserJson user) {
    allPeoplePage.checkSubmitInvitationBtn(user.testData().friendName());
  }

  @DisplayName("Проверим, что есть кнопка отклонить запрос в друзья")
  @Test
  void invitationTableShouldHaveDeclineInvitationBtn(@MyUserQueue(INVITATION_RECIEVED) UserJson user) {
    allPeoplePage.checkDeclineInvitationBtn(user.testData().friendName());
  }

  @DisplayName("Проверим, что есть аватар")
  @Test
  void invitationTableShouldHaveAvatar(@MyUserQueue(INVITATION_RECIEVED) UserJson user) {
    allPeoplePage.checkAvatar(user.testData().friendName());
  }


}
