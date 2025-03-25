package com.shwotime.userservice.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class ApiResponse<T> {

    private Boolean success;
    private String message;
    private T data;
    private String path;
    private String errorCode;


    public static <T> ApiResponse<T> ok(T data,String path){
        return new ApiResponse<>(true,"",data,path,"");
    }

    public static <T> ApiResponse<T> error(String message,String path,String errorCode){
        return new ApiResponse<>(false,message,null,path,errorCode);
    }


}
