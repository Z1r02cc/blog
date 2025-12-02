package com.zero.blog.util;

import lombok.Data;

@Data()
public  class CommonResult<T> {

    private  int code;
    private  String message;
    private T data;

    private CommonResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private CommonResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(200, "操作成功", data);
    }

    public static <T> CommonResult<T> success(String msg){
        return new CommonResult<T>(200,msg);
    }
    public static <T> CommonResult<T> failed(String msg){
        return new CommonResult<T>(403,msg);
    }


}
