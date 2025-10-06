package travel.winwin.dataapi.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import travel.winwin.dataapi.config.SecurityProperties;

@Component
public class InternalTokenFilter implements Filter {

  private final String expectedToken;

  public InternalTokenFilter(SecurityProperties properties) {
    expectedToken = properties.getInternalToken();
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;

    String actualToken = httpRequest.getHeader("X-Internal-Token");

    if (expectedToken.equals(actualToken)) {
      chain.doFilter(request, response);
    } else {
      HttpServletResponse httpResponse = (HttpServletResponse) response;
      httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Internal token is invalid");
    }
  }
}
