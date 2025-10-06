package travel.winwin.authapi.filtet;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import travel.winwin.authapi.service.JwtTokenService;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenService jwtTokenService;

  public JwtAuthenticationFilter(JwtTokenService jwtTokenService) {
    this.jwtTokenService = jwtTokenService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, @NonNull  HttpServletResponse response, @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    String token = null;

    String path = request.getRequestURI();
    String method = request.getMethod();

    log.debug("JwtAuthenticationFilter processing request to {} {}", method, path);

    if (request.getCookies() == null) {
      log.warn("No cookies found in request to {} {}", method, path);
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Token is required");
      return;
    }

    for (Cookie cookie : request.getCookies()) {
      if ("authToken".equals(cookie.getName())) {
        token = cookie.getValue();
        log.debug("authToken was found in cookies in request to {} {}", method, path);
        break;
      }
    }

    if (!StringUtils.hasText(token)) {
      log.warn("Token was found in request to {} {}, but it's empty", method, path);
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Token is required");
      return;
    }

    if (!jwtTokenService.isValidToken(token)) {
      log.warn("Token is invalid in request to {} {}", method, path);
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Invalid token");
      return;
    }

    String email = jwtTokenService.getEmailFromToken(token);
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
    SecurityContextHolder.getContext().setAuthentication(authentication);

    log.debug("Successfully authenticated request to {} {}", method, path);
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getRequestURI();
    String method = request.getMethod();
    boolean shouldSkip = !path.startsWith("/api/v1/auth/process");

    if (shouldSkip) {
      log.debug("Skipping Jwt filter in request to {} {}", method, path);
    }

    return shouldSkip;
  }
}
