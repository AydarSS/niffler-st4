package aydarss.fork.niffler.aytest;


import aydarss.fork.niffler.ayjupiter.ayextension.BrowserExtension;
import aydarss.fork.niffler.aypage.AllPeoplePage;
import aydarss.fork.niffler.aypage.FriendsPage;
import aydarss.fork.niffler.aypage.LoginPage;
import aydarss.fork.niffler.aypage.MainPage;
import aydarss.fork.niffler.aypage.WelcomePage;
import com.codeborne.selenide.Configuration;
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
