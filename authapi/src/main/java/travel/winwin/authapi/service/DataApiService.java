package travel.winwin.authapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import travel.winwin.authapi.exception.DataApiException;

@Service
public class DataApiService {

  private final RestTemplate restTemplate;

  @Value("${dataapi.url}")
  private String dataApiBaseUrl;

  @Value("${app.security.internal-token}")
  private String internalToken;

  public DataApiService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @SuppressWarnings("deprecation")
  public String transformText(String text) {
    String url =
        UriComponentsBuilder.fromHttpUrl(dataApiBaseUrl)
            .path("/api/v1/transform/")
            .path(text)
            .toUriString();

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("X-Internal-Token", internalToken);

    HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

    try {
      ResponseEntity<String> response =
          restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
      return response.getBody();
    } catch (RestClientException e) {
      throw new DataApiException("Data Api Communication Error", e, HttpStatus.INTERNAL_SERVER_ERROR, null, url);
    }
  }
}
