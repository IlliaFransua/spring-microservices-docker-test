package travel.winwin.authapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import travel.winwin.authapi.dto.request.TransformTextRequest;
import travel.winwin.authapi.service.DataApiService;
import travel.winwin.authapi.service.UserService;

@Slf4j
@RestController
@RequestMapping("/api/v1/data")
public class DataController {

  private final UserService userService;
  private final DataApiService dataApiService;

  public DataController(UserService userService, DataApiService dataApiService) {
    this.userService = userService;
    this.dataApiService = dataApiService;
  }

  @PostMapping("/process")
  public ResponseEntity<String> process(@RequestBody TransformTextRequest request) {
    log.info("Processing text transform request. Text length = {}", request.text().length());

    String inputText = request.text();
    String outputText = dataApiService.transformText(request.text());

    log.debug("Text transformation completed successfully. Text length = {}", outputText.length());

    userService.logUserTextTransformResult(inputText, outputText);

    return ResponseEntity.ok(outputText);
  }
}
