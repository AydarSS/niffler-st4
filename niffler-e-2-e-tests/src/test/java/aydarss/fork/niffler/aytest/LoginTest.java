package aydarss.fork.niffler.aytest;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

import aydarss.fork.niffler.aydb.aymodel.Authority;
import aydarss.fork.niffler.aydb.aymodel.AuthorityEntity;
import aydarss.fork.niffler.aydb.aymodel.UserAuthEntity;
import aydarss.fork.niffler.aydb.aymodel.UserEntity;
import aydarss.fork.niffler.aydb.ayrepository.UserRepository;
import aydarss.fork.niffler.ayjupiter.ayextension.UserRepositoryExtension;
import aydarss.fork.niffler.aymodel.CurrencyValues;
import aydarss.fork.niffler.aypage.LoginPage;
import aydarss.fork.niffler.aypage.MainPage;
import com.codeborne.selenide.Selenide;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UserRepositoryExtension.class)
public class LoginTest extends BaseWebTest {

  private UserRepository userRepository;

  private UserAuthEntity userAuth;
  private UserEntity user;


  @BeforeEach
  void createUser() {
    userAuth = new UserAuthEntity();
    userAuth.setUsername("valentin_15");
    userAuth.setPassword("12345");
    userAuth.setEnabled(true);
    userAuth.setAccountNonExpired(true);
    userAuth.setAccountNonLocked(true);
    userAuth.setCredentialsNonExpired(true);

    AuthorityEntity[] authorities = Arrays.stream(Authority.values()).map(
        a -> {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setAuthority(a);
          return ae;
        }
    ).toArray(AuthorityEntity[]::new);

    userAuth.addAuthorities(authorities);

    user = new UserEntity();
    user.setUsername("valentin_15");
    user.setCurrency(CurrencyValues.RUB);
    userRepository.createInAuth(userAuth);
    userRepository.createInUserdata(user);
  }

  @AfterEach
  void removeUser() {
    userRepository.deleteInAuthById(userAuth.getId());
    userRepository.deleteInUserdataById(user.getId());
  }


  @Test
  @DisplayName("Стастистика должна быть видна после авторизации")
  void statisticShouldBeVisibleAfterLogin() {
    Selenide.open("http://127.0.0.1:3000/main");
    $("a[href*='redirect']").click();

    loginPage
        .setLogin(userAuth.getUsername())
        .setPassword(userAuth.getPassword())
        .submit();

    mainPage
        .checkThatStatisticDisplayed();
  }
}