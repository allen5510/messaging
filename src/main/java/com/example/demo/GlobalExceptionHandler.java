package com.example.demo;

import com.example.demo.controller.util.ResponseCode;
import com.example.demo.controller.util.ResponseHandler;
import com.example.demo.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity handleException(Exception e) {
        e.printStackTrace();
        return ResponseHandler.responseFail(ResponseCode.SYSTEM_ERROR, "unknown error");
    }

    @ExceptionHandler(value = FormatException.class)
    public ResponseEntity handleFormatException(FormatException e) {
        return ResponseHandler.responseFail(ResponseCode.FORMAT_ERROR, e.getMessage());
    }

    @ExceptionHandler(value = TokenException.class)
    public ResponseEntity handleTokenException(TokenException e) {
        return ResponseHandler.responseFail(ResponseCode.TOKEN_ERROR, e.getMessage());
    }

    @ExceptionHandler(value = LoginException.class)
    public ResponseEntity handleLoginException(LoginException e) {
        return ResponseHandler.responseFail(ResponseCode.LOGIN_FAIL, e.getMessage());
    }

    @ExceptionHandler(value = PermissionException.class)
    public ResponseEntity handlePermissionException(PermissionException e) {
        return ResponseHandler.responseFail(ResponseCode.PERMISSION_DENIED, e.getMessage());
    }

    @ExceptionHandler(value = DuplicateException.class)
    public ResponseEntity handleDuplicateException(DuplicateException e) {
        return ResponseHandler.responseFail(ResponseCode.DUPLICATE, e.getMessage());
    }

    @ExceptionHandler(value = OperationException.class)
    public ResponseEntity handleOperationException(OperationException e) {
        return ResponseHandler.responseFail(ResponseCode.OPERATION_NOT_ALLOWED, e.getMessage());
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException e) {
        return ResponseHandler.responseFail(ResponseCode.NOT_FOUND, e.getMessage());
    }

}
