package study.spring.websocket.bookservice.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookValidator<T> {

  private final Validator validator;

  public void validate(T body) {
    Errors errors =
        new BeanPropertyBindingResult(body, body.getClass().getName());

    this.validator.validate(body, errors);

    if (!errors.getAllErrors().isEmpty()) {
      onValidationErrors(errors);
    }
  }

  private void onValidationErrors(Errors errors) {
    String errorMessage = errors.getAllErrors().toString();
    log.error(errorMessage);
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
  }
}
