package travel.winwin.authapi.service;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import travel.winwin.authapi.exception.DatabaseException;
import travel.winwin.authapi.model.ProcessingLog;
import travel.winwin.authapi.model.User;
import travel.winwin.authapi.repository.ProcessingLogRepository;
import travel.winwin.authapi.repository.UserRepository;

@Slf4j
@Service
public class UserService {

  private final UserRepository userRepository;
  private final ProcessingLogRepository processingLogRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, ProcessingLogRepository processingLogRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.processingLogRepository = processingLogRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public void register(String email, String rawPassword) {
    if (findByEmail(email).isPresent()) {
      throw new DatabaseException(
          "Registration is failed. User already exists with email: " + email);
    }
    String hashedPassword = passwordEncoder.encode(rawPassword);
    User user = new User();
    user.setEmail(email);
    user.setPasswordHash(hashedPassword);
    userRepository.save(user);
    log.info("User registered successfully: {}", email);
  }

  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public boolean checkPassword(String rawPassword, String hashedPassword, String email) {
    if (passwordEncoder.matches(rawPassword, hashedPassword)) {
      log.info("User {} successfully authenticated (password match)", email);
      return true;
    } else {
      log.warn("Authentication failed for user {}. Provided password is wrong", email);
      return false;
    }
  }

  public void logUserTextTransformResult(String inputText, String outputText) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();

    Optional<User> optionalUser = findByEmail(email);
    if (optionalUser.isEmpty()) {
      throw new DatabaseException("User is authenticated but UserService.findByEmail cause null");
    }

    User user = optionalUser.get();
    ProcessingLog processingLog = new ProcessingLog();
    processingLog.setUser(user);
    processingLog.setInputText(inputText);
    processingLog.setOutputText(outputText);
    processingLogRepository.save(processingLog);
    log.info("Saved processing log to db {}", user.getEmail());
  }
}
