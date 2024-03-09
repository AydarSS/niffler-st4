package aydarss.fork.niffler.aytest.web;

import static aydarss.fork.niffler.ayjupiter.ayannotation.MyUserQueue.UserType.WITH_FRIENDS;

import aydarss.fork.niffler.ayjupiter.ayannotation.MyUserQueue;
import aydarss.fork.niffler.ayjupiter.ayextension.UsersQueueExtension;
import com.codeborne.selenide.Selenide;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UsersQueueExtension.class)
public class FriendsTest extends BaseWebTest {

  @BeforeEach
  void doLogin(@MyUserQueue(WITH_FRIENDS) UserJson user) {
    Selenide.open("http://127.0.0.1:3000/main");
    welcomePage.clickLoginBtn();
    loginPage.loginByUserAndPassword(user.username(), user.testData().password());
    mainPage
        .checkThatStatisticDisplayed()
        .getHeader()
        .goToFriendsPage();
  }

  @DisplayName("Проверим, что есть друг и его имя совпадает с ожидаемым")
  @Test
  void friendsTableShouldNotBeEmpty(@MyUserQueue(WITH_FRIENDS) UserJson user) {
    friendsPage.findRecordInFriendsTable(user.testData().friendName());
  }

  @DisplayName("Проверим, что есть запись о друзьях")
  @Test
  void friendsTableShouldHaveRecordAboutFriends(@MyUserQueue(WITH_FRIENDS) UserJson user) {
    friendsPage.checkFriendsStatus(user.testData().friendName());
  }

  @DisplayName("Проверим, что количество друзей равно 1")
  @Test
  void friendsTableShouldHaveOneRecord() {
    friendsPage.checkCountFriends(1);
  }

  @DisplayName("Проверим, что есть кнопка Удалить")
  @Test
  void friendsTableShouldHaveRemobeBtn(@MyUserQueue(WITH_FRIENDS) UserJson user) {
    friendsPage.checkRemoveBtn(user.testData().friendName());
  }

  @DisplayName("Проверим, что есть аватар")
  @Test
  void friendsTableShouldHaveAvatar(@MyUserQueue(WITH_FRIENDS) UserJson user) {
    friendsPage.checkAvatar(user.testData().friendName());
  }

}
