package travel.winwin.authapi.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import travel.winwin.authapi.dto.response.ErrorResponse;
import travel.winwin.authapi.exception.DataApiException;
import travel.winwin.authapi.exception.DatabaseException;

// TODO: Improve exceptions architecture to give more information to the client app
//  (not just "Internal server error"). For example the DatabaseException is actually
//  an exception for already registered users. So we can for example create crown class
//  AlreadyRegisteredUserException / AlreadyExistsUserException and so one.
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DataApiException.class)
  public ResponseEntity<ErrorResponse> handleDataApiException(DataApiException e) {
    log.error("DataApi unexpected error: url={} -> {}", e.getUrl(), e.getMessage(), e);

    return ResponseEntity.status(e.getStatusCode())
        .body(new ErrorResponse("Internal server error", e.getStatusCode()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnknownException(Exception e) {
    log.error("Unexpected error", e);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR));
  }

  @ExceptionHandler(DatabaseException.class)
  public ResponseEntity<ErrorResponse> handleDatabaseException(DatabaseException e) {
    log.warn("DatabaseError: {} - Status: {}", e.getMessage(), e.getStatusCode());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR));
  }
}
