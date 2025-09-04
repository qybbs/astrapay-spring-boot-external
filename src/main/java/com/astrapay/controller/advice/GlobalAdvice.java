package com.astrapay.controller.advice;

import com.astrapay.dto.response.BaseResponse;
import com.astrapay.exception.NoteNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(
                new BaseResponse<>(
                        false,
                        ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleNoteNotFound(NoteNotFoundException ex) {
        return new ResponseEntity<>(
                new BaseResponse<>(
                        false,
                        ex.getMessage()
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<String>> handleGeneric(Exception ex) {
        return new ResponseEntity<>(
                new BaseResponse<>(
                        false,
                        ex.getMessage()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
