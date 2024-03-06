package guru.qa.niffler.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SpecificRequestDumperFilterTest {

  private SpecificRequestDumperFilter specificRequestDumperFilter;

  GenericFilter decorate = Mockito.mock();

  @Test
  void doFilterWithNotHttpServletRequest(@Mock ServletRequest servletRequest,
      @Mock ServletResponse response,
      @Mock FilterChain chain) throws ServletException, IOException {

    String[] urlPatterns = {"url", "url2"};
    specificRequestDumperFilter = new SpecificRequestDumperFilter(decorate,urlPatterns);

    specificRequestDumperFilter.doFilter(servletRequest, response, chain);

    verify(decorate, times(0)).doFilter(servletRequest, response,chain);
    verify(chain, times(1)).doFilter(servletRequest, response);
  }

  @Test
  void doFilterWithHttpServletRequestHasOneMatchWithUri(@Mock HttpServletRequest httpServletRequest,
      @Mock ServletResponse response,
      @Mock FilterChain chain) throws ServletException, IOException {
    String[] urlPatterns = {"url", "url2"};
    specificRequestDumperFilter = new SpecificRequestDumperFilter(decorate,urlPatterns);

    when(httpServletRequest.getRequestURI()).thenReturn("url");

    specificRequestDumperFilter.doFilter(httpServletRequest, response, chain);

    verify(decorate, times(1)).doFilter(httpServletRequest, response,chain);
    verify(chain, times(0)).doFilter(httpServletRequest, response);
  }

  @Test
  void doFilterWithHttpServletRequestHasManyMatchesWithUri(@Mock HttpServletRequest httpServletRequest,
      @Mock ServletResponse response,
      @Mock FilterChain chain) throws ServletException, IOException {

    String[] urlPatterns = {"url", ".*"};
    specificRequestDumperFilter = new SpecificRequestDumperFilter(decorate,urlPatterns);

    when(httpServletRequest.getRequestURI()).thenReturn("url");

    specificRequestDumperFilter.doFilter(httpServletRequest, response, chain);

    verify(decorate, times(1)).doFilter(httpServletRequest, response,chain);
    verify(chain, times(0)).doFilter(httpServletRequest, response);
  }

  @Test
  void doFilterWithHttpServletRequestHasNoMatchesWithUri(@Mock HttpServletRequest httpServletRequest,
      @Mock ServletResponse response,
      @Mock FilterChain chain) throws ServletException, IOException {

    String[] urlPatterns = {"notUrl", "notUrl1"};
    specificRequestDumperFilter = new SpecificRequestDumperFilter(decorate,urlPatterns);

    when(httpServletRequest.getRequestURI()).thenReturn("url");

    specificRequestDumperFilter.doFilter(httpServletRequest, response, chain);

    verify(decorate, times(0)).doFilter(httpServletRequest, response,chain);
    verify(chain, times(1)).doFilter(httpServletRequest, response);
  }

  @Test
  void destroy() {
    String[] urlPatterns = {"url", ".*"};
    specificRequestDumperFilter = new SpecificRequestDumperFilter(decorate,urlPatterns);

    specificRequestDumperFilter.destroy();

    verify(decorate, times(1)).destroy();
  }
}