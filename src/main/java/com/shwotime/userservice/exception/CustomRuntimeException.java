package com.shwotime.userservice.exception;


import com.shwotime.userservice.type.ErrorCode;
import lombok.Getter;

@Getter
public class CustomRuntimeException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomRuntimeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
    public CustomRuntimeException(String code){
        this.errorCode = ErrorCode.fromCode(code);
    }
}
