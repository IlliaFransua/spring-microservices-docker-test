package travel.winwin.authapi.dto.response;

import java.time.Instant;
import org.springframework.http.HttpStatus;

public record ErrorResponse(String error, int statusCode, Instant timeStamp) {
  public ErrorResponse(String error, HttpStatus status) {
    this(error, status.value(), Instant.now());
  }
}
