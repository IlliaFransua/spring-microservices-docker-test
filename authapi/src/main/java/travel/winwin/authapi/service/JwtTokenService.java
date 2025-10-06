package travel.winwin.authapi.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

  private final SecretKey secret;

  private final long jwtExpirationMs;

  public long getJwtExpirationMs() {
    return jwtExpirationMs;
  }

  public JwtTokenService(
      @Value("${jwt.secret}") String secret, @Value("${jwt.expiration-ms}") long jwtExpirationMs) {
    byte[] decodedKey = Base64.getDecoder().decode(secret);
    this.secret = Keys.hmacShaKeyFor(decodedKey);
    this.jwtExpirationMs = jwtExpirationMs;
  }

  @SuppressWarnings("deprecation")
  public String createToken(String email) {
    Instant now = Instant.now();
    Instant expiry = now.plusMillis(jwtExpirationMs);

    return Jwts.builder()
        .setSubject(email)
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(expiry))
        .signWith(secret)
        .compact();
  }

  @SuppressWarnings("deprecation")
  public boolean isValidToken(String token) {
    try {
      Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @SuppressWarnings("deprecation")
  public String getEmailFromToken(String token) {
    Claims claims = Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token).getBody();

    return claims.getSubject();
  }
}
