package aydarss.fork.niffler.aypage;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

public class Header {

  private final SelenideElement mainBtn = $("a[href*='main']");
  private final SelenideElement friendsBtn = $("a[href*='friends']");
  private final SelenideElement peopleBtn = $("a[href*='people']");
  private final SelenideElement profileBtn = $("a[href*='profile']");
  private final SelenideElement logoutBtn = $(".button-icon.button-icon_type_logout");


  @Step("Переход на главную страницу")
  public void goToMainPage() {
    mainBtn.click();
  }

  @Step("Переход на страницу друзей")
  public void goToFriendsPage(){
    friendsBtn.click();
  }

  @Step("Переход на страницу все люди")
  public void goToPeoplePage() {
    peopleBtn.click();
  }

  @Step("Переход на страницу профиля")
  public void goToProfilePage() {
    profileBtn.click();
  }

  @Step("Выйти из системы")
  public void logout() {
    logoutBtn.click();
  }
}
