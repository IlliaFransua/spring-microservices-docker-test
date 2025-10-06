package travel.winwin.authapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DatabaseException extends RuntimeException {

  private final HttpStatus statusCode;

  public DatabaseException(String message, Throwable cause, HttpStatus statusCode) {
    super(message, cause);
    this.statusCode = statusCode;
  }

  public DatabaseException(String message, HttpStatus statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

}
