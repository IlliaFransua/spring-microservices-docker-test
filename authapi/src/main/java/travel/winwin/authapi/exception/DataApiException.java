package travel.winwin.authapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DataApiException extends RuntimeException {

  public DataApiException(String message, Throwable cause) {
    super(message, cause);
  }
}

