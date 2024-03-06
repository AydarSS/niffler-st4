package aydarss.fork.niffler.aytest.web;

import com.codeborne.selenide.Selenide;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RegisterTest extends BaseWebTest {

  @Test
  void testForExistingUserShouldBeErrorMessageVisible() {
    Selenide.open("http://127.0.0.1:3000/main");
    welcomePage
        .clickRegisterBtn();

    registerPage
        .setUsername("duck")
        .setPassword("12345")
        .setPasswordSubmit("12345")
        .clickSubmitBtnForExistUser()
        .checkUserAlreadyExistsMsg();
  }

  @Test
  void testSuccessRegister() {
    Selenide.open("http://127.0.0.1:3000/main");
    welcomePage
        .clickRegisterBtn();

    registerPage
        .setUsername(UUID.randomUUID().toString())
        .setPassword("12345")
        .setPasswordSubmit("12345")
        .clickSubmitBtn()
        .checkregisterSuccessMsg();
  }

  @Test
  @DisplayName("При неправильном вводе пароля второй раз, должно быть сообщение об ошибке")
  void testPasswordsNotEqualShouldBeErrorMessageVisible() {
    Selenide.open("http://127.0.0.1:3000/main");
    welcomePage
        .clickRegisterBtn();

    registerPage
        .setUsername(UUID.randomUUID().toString())
        .setPassword("11111")
        .setPasswordSubmit("12345")
        .clickSubmitBtnForExistUser()
        .checkPasswordsNotEqualMsg();
  }

  @Test
  @DisplayName("Если пароль меньше 3 символов должно показываться сообщение об ошибке")
  void testPasswordLengthIsShortShouldBeErrorMessageVisible() {
    Selenide.open("http://127.0.0.1:3000/main");
    welcomePage
        .clickRegisterBtn();

    registerPage
        .setUsername(UUID.randomUUID().toString())
        .setPassword("11")
        .setPasswordSubmit("11")
        .clickSubmitBtnForExistUser()
        .checkPasswordLengthMsg();
  }

}
