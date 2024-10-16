package ru.yterinc.CustomerContactInformation.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import java.util.List;

public class ErrorUtility {
    public static void getErrorBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new NotCreatedException(errorMsg.toString());  // выбрасываем исключение,
            // содержащие сообщения об ошибке
        }
    }
}
