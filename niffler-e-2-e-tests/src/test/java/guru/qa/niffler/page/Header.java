package guru.qa.niffler.page;

import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.SelenideElement;

public class Header {

  private final SelenideElement mainBtn = $("a[href*='main']");
  private final SelenideElement friendsBtn = $("a[href*='friends']");
  private final SelenideElement peopleBtn = $("a[href*='people']");
  private final SelenideElement profileBtn = $("a[href*='profile']");
  private final SelenideElement logoutBtn = $(".button-icon.button-icon_type_logout");


  public void goToMainPage() {
    mainBtn.click();
  }

  public void goToFriendsPage(){
    friendsBtn.click();
  }

  public void goToPeoplePage() {
    peopleBtn.click();
  }

  public void goToProfilePage() {
    profileBtn.click();
  }

  public void logout() {
    logoutBtn.click();
  }
}
