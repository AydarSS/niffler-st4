package aydarss.fork.niffler.aypage;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;

public class FriendsPage extends BasePage<FriendsPage> {

  private final ElementsCollection friendsTable = $(".abstract-table tbody").$$("tr");

  @Step("Проверить наличие записи {text} в таблице друзей")
  public void findRecordInFriendsTable (String text) {
    friendsTable.find(text(text)).shouldBe(visible);
  }

  @Step("Проверить отображение друзей")
  public void checkFriendsStatus(String text) {
    friendsTable.find(text(text)).$(byText("You are friends")).shouldBe(visible);
  }

  @Step("Проверяем, что количество друзей равно {count}")
  public void checkCountFriends(int count) {
    friendsTable.shouldHave(size(count));
  }

  @Step("Проверяем, что есть аватар")
  public void checkAvatar(String text) {
    friendsTable.find(text(text)).$(".people__user-avatar").shouldBe(visible);
  }

  @Step("Проверяем, что есть кнопка удалить из друзей")
  public void checkRemoveBtn(String text) {
    friendsTable.find(text(text)).$("[data-tooltip-id='remove-friend']").shouldBe(visible);
  }
}
