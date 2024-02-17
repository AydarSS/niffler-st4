package aydarss.fork.niffler.aytest;


import aydarss.fork.niffler.ayjupiter.ayextension.BrowserExtension;
import aydarss.fork.niffler.aypage.AllPeoplePage;
import aydarss.fork.niffler.aypage.FriendsPage;
import aydarss.fork.niffler.aypage.LoginPage;
import aydarss.fork.niffler.aypage.MainPage;
import aydarss.fork.niffler.aypage.ProfilePage;
import aydarss.fork.niffler.aypage.RegisterPage;
import aydarss.fork.niffler.aypage.WelcomePage;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({BrowserExtension.class})
public abstract class BaseWebTest {

  WelcomePage welcomePage = new WelcomePage();
  LoginPage loginPage = new LoginPage();
  MainPage mainPage = new MainPage();
  FriendsPage friendsPage = new FriendsPage();
  AllPeoplePage allPeoplePage = new AllPeoplePage();
  ProfilePage profilePage = new ProfilePage();
  RegisterPage registerPage = new RegisterPage();
}
