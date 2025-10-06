package travel.winwin.authapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DataApiException extends RuntimeException {
  private final HttpStatus status;
  private final String responseBody;
  private final String url;

  public DataApiException(String message, Throwable cause, HttpStatus status, String responseBody, String url) {
    super(message, cause);
    this.status = status;
    this.responseBody = responseBody;
    this.url = url;
  }

  public HttpStatus getStatusCode() { return status; }
  public String getResponseBody() { return responseBody; }
  public String getUrl() { return url; }
}

