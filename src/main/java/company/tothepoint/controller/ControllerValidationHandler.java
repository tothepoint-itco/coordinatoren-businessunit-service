package company.tothepoint.controller;

import company.tothepoint.model.MessageDTO;
import company.tothepoint.model.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerValidationHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<MessageDTO> processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> errors = result.getFieldErrors();

        return processFieldError(errors);
    }

    private List<MessageDTO> processFieldError(List<FieldError> errors) {
        Locale currentLocale = LocaleContextHolder.getLocale();

        return errors.stream().map(fieldError -> {
            String msg = messageSource.getMessage(fieldError.getDefaultMessage(), null, currentLocale);
            return new MessageDTO(MessageType.ERROR, msg);
        }).collect(Collectors.toList());
    }
}
