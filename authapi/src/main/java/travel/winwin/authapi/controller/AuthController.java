package travel.winwin.authapi.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import travel.winwin.authapi.dto.request.LoginRequest;
import travel.winwin.authapi.dto.request.RegisterRequest;
import travel.winwin.authapi.dto.request.TransformTextRequest;
import travel.winwin.authapi.dto.response.JwtResponse;
import travel.winwin.authapi.service.DataApiService;
import travel.winwin.authapi.service.JwtTokenService;
import travel.winwin.authapi.service.UserService;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final UserService userService;
  private final JwtTokenService jwtTokenService;

  @Value("${cookie.secure:true}")
  private boolean isSecureCookie;

  public AuthController(
      UserService userService, JwtTokenService jwtTokenService) {
    this.userService = userService;
    this.jwtTokenService = jwtTokenService;
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
    String receivedEmail = request.email();
    userService.register(receivedEmail, request.password());
    return ResponseEntity.ok("User registered successfully");
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
    return userService
        .findByEmail(request.email())
        .map(
            user -> {
              if (userService.checkPassword(request.password(), user.getPasswordHash(), user.getEmail())) {
                String token = jwtTokenService.createToken(user.getEmail());

                ResponseCookie cookie =
                    ResponseCookie.from("authToken", token)
                        .httpOnly(true)
                        .secure(isSecureCookie)
                        .path("/")
                        .maxAge(jwtTokenService.getJwtExpirationMs())
                        .sameSite("Strict")
                        .build();

                response.addHeader("Set-Cookie", cookie.toString());

                return ResponseEntity.ok(new JwtResponse(token));
              } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
              }
            })
        .orElseGet(() -> {
          log.warn("Login failed: {} not found", request.email());
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        });
  }
}
