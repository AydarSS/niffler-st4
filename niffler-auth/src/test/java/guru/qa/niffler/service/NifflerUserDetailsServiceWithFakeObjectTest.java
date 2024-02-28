package guru.qa.niffler.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import guru.qa.niffler.data.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


class NifflerUserDetailsServiceWithFakeObjectTest {

  private NifflerUserDetailsService nifflerUserDetailsService;

  @BeforeEach
  void initMockRepository() {
    UserRepository userRepository = new UserRepository.Fake();
    nifflerUserDetailsService = new NifflerUserDetailsService(userRepository);
  }

  @Test
  void loadUserByUsername() {
    final UserDetails correct = nifflerUserDetailsService.loadUserByUsername("correct");

    final List<SimpleGrantedAuthority> expectedAuthorities = correct.getAuthorities().stream()
        .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
        .toList();

    assertEquals(
        "correct",
        correct.getUsername()
    );
    assertEquals(
        "test-pass",
        correct.getPassword()
    );
    assertEquals(
        expectedAuthorities,
        correct.getAuthorities()
    );

    assertTrue(correct.isAccountNonExpired());
    assertTrue(correct.isAccountNonLocked());
    assertTrue(correct.isCredentialsNonExpired());
    assertTrue(correct.isEnabled());
  }

  @Test
  void loadUserByUsernameNegayive() {
    final UsernameNotFoundException exception = assertThrows(
        UsernameNotFoundException.class,
        () -> nifflerUserDetailsService.loadUserByUsername("incorrect")
    );

    assertEquals(
        "Username: incorrect not found",
        exception.getMessage()
    );
  }
}