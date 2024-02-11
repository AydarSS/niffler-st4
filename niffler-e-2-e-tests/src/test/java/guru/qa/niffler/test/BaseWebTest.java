package guru.qa.niffler.test;


import com.codeborne.selenide.Configuration;
import guru.qa.niffler.page.AllPeoplePage;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.WelcomePage;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({BrowserExtension.class})
public abstract class BaseWebTest {

  static {
    Configuration.browserSize = "1980x1024";
  }

  WelcomePage welcomePage = new WelcomePage();
  LoginPage loginPage = new LoginPage();
  MainPage mainPage = new MainPage();
  FriendsPage friendsPage = new FriendsPage();
  AllPeoplePage allPeoplePage = new AllPeoplePage();
}
