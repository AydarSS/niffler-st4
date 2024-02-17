package aydarss.fork.niffler.aypage;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;

public class AllPeoplePage extends BasePage<AllPeoplePage> {

  private final ElementsCollection invitationTable = $(".abstract-table tbody").$$("tr");

  @Step("Найти запись в таблице друзей")
  public AllPeoplePage findRecordInFriendsTable(String text) {
    invitationTable.find(text(text)).shouldBe(visible);
    return this;
  }

  @Step("Проверить наличие заявки в друзья")
  public AllPeoplePage checkPendingInvitation(String text) {
    invitationTable.find(text(text)).$(byText("Pending invitation")).shouldBe(visible);
    return this;
  }

  @Step("Проверить количество записей в заявке в друзья")
  public AllPeoplePage checkCountInvitation(int count) {
    invitationTable.shouldHave(size(count));
    return this;
  }

  @Step("Проверить наличие аватара")
  public AllPeoplePage checkAvatar(String text) {
    invitationTable.find(text(text)).$(".people__user-avatar").shouldBe(visible);
    return this;
  }

  @Step("Проверить наличие кнопки принять в друзья")
  public AllPeoplePage checkSubmitInvitationBtn(String text) {
    invitationTable.find(text(text)).$(".button-icon_type_submit").shouldBe(visible);
    return this;
  }

  @Step("Проверить наличие кнопки отклонить заявку в друзья")
  public AllPeoplePage checkDeclineInvitationBtn(String text) {
    invitationTable.find(text(text)).$(".button-icon_type_close").shouldBe(visible);
    return this;
  }


}
