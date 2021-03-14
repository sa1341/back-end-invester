package com.kakaopay.investment.controllerAdvice;

import com.kakaopay.investment.domain.item.exception.ItemNotFoundException;
import com.kakaopay.investment.domain.member.exception.MemberNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class RestControllerExceptionAdvice {

    @ExceptionHandler(value = MemberNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleMemberNotFound(MemberNotFoundException e) {
        log.error("Member is not exist", e);
        ErrorResponse responseError = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(responseError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ItemNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleItemsNotFound(ItemNotFoundException e) {
        log.error("Item is not exist", e);
        ErrorResponse responseError = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(responseError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception: {}", e);
        ErrorResponse responseError = new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(responseError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        log.error("MethodArgumentNotValidException: {}", e);
        ErrorResponse responseError = new ErrorResponse(errorMessage, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
    }
}
