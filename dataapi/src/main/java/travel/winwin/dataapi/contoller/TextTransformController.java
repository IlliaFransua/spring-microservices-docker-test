package travel.winwin.dataapi.contoller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import travel.winwin.dataapi.dto.response.TextTransformResponse;

@RestController
@RequestMapping("/api/v1/transform")
public class TextTransformController {

  @PostMapping("/{text}")
  public TextTransformResponse transform(@PathVariable String text) {
    String transformedText =
        new StringBuilder(text).reverse().insert(0, "prefix_").toString().toUpperCase();
    return new TextTransformResponse(transformedText);
  }
}
