package travel.winwin.authapi.dto.response;

import java.time.Instant;
import org.springframework.http.HttpStatus;

public record ErrorResponse(String error, Instant timeStamp) {
  public ErrorResponse(String error) {
    this(error, Instant.now());
  }
}
