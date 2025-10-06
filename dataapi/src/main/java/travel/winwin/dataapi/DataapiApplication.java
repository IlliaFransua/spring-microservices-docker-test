package travel.winwin.dataapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(travel.winwin.dataapi.config.SecurityProperties.class)
public class DataapiApplication {

  public static void main(String[] args) {
    SpringApplication.run(DataapiApplication.class, args);
  }
}
