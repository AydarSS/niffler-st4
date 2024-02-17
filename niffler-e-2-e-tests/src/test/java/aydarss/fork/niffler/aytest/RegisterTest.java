package aydarss.fork.niffler.aytest;

import aydarss.fork.niffler.aypage.RegisterPage;
import aydarss.fork.niffler.aypage.WelcomePage;
import com.codeborne.selenide.Selenide;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class RegisterTest extends BaseWebTest {

  @Test
  void testForExistingUserShouldBeErrorMessageVisible() {
    Selenide.open("http://127.0.0.1:3000/main");
    new WelcomePage()
        .clickRegisterBtn();

    new RegisterPage()
        .setUsername("duck")
        .setPassword("12345")
        .setPasswordSubmit("12345")
        .clickSubmitBtnForExistUser()
        .checkUserAlreadyExistsMsg();
  }

  @Test
  void testSuccessRegister() {
    Selenide.open("http://127.0.0.1:3000/main");
    new WelcomePage()
        .clickRegisterBtn();

    new RegisterPage()
        .setUsername(UUID.randomUUID().toString())
        .setPassword("12345")
        .setPasswordSubmit("12345")
        .clickSubmitBtn()
        .checkregisterSuccessMsg();
  }

  @Test
  void testPasswordsNotEqualShouldBeErrorMessageVisible() {
    Selenide.open("http://127.0.0.1:3000/main");
    new WelcomePage()
        .clickRegisterBtn();

    new RegisterPage()
        .setUsername(UUID.randomUUID().toString())
        .setPassword("11111")
        .setPasswordSubmit("12345")
        .clickSubmitBtnForExistUser()
        .checkPasswordsNotEqualMsg();
  }

  @Test
  void testPasswordLengthIsShortShouldBeErrorMessageVisible() {
    Selenide.open("http://127.0.0.1:3000/main");
    new WelcomePage()
        .clickRegisterBtn();

    new RegisterPage()
        .setUsername(UUID.randomUUID().toString())
        .setPassword("11")
        .setPasswordSubmit("11")
        .clickSubmitBtnForExistUser()
        .checkPasswordLengthMsg();
  }

}
