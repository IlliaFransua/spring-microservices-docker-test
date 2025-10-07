package travel.winwin.authapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DatabaseException extends RuntimeException {

  public DatabaseException(String message) {
    super(message);
  }

  public DatabaseException(String message, Throwable cause) {
    super(message, cause);
  }

}
