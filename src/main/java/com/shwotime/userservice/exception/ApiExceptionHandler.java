package com.shwotime.userservice.exception;

import com.shwotime.userservice.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(CustomRuntimeException.class)
    protected ResponseEntity<ApiResponse<String>> handleCustomException(CustomRuntimeException e){

        return ResponseEntity.status(e.getErrorCode().getStatus()).body(ApiResponse.error(e.getErrorCode().getMessage(),"",e.getErrorCode().getCode()));

    }


}
