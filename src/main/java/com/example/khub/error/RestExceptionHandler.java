package com.example.khub.error;

import com.example.khub.error.exceptions.ImageNotFoundException;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ImageNotFoundException.class})
    public ResponseEntity<CustomErrorMessage> handleImageNotFoundException(ImageNotFoundException ex) {
        return new ResponseEntity<>(new CustomErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @Getter
    @Setter
    private static class CustomErrorMessage {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        private Date timestamp;
        private int code;
        private String status;
        private String message;

        public CustomErrorMessage(HttpStatus status, String message) {
            timestamp = new Date();
            this.code = status.value();
            this.status = status.getReasonPhrase();
            this.message = message;
        }
    }
}
