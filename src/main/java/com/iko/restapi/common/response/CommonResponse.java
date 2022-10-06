package com.iko.restapi.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.iko.restapi.common.exception.BaseException;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CommonResponse<T> {
    private int code;
    private String msg;
    private T data;

    protected CommonResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> CommonResponse<T> ok() {
        return new CommonResponse(200, "ok", null);
    }

    public static <T> CommonResponse<T> ok(String msg) {
        return new CommonResponse(200, msg, null);
    }

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse(200, "ok", data);
    }

    public static <T> CommonResponse<T> fail(BaseException e) {
        return new CommonResponse(e.getErrorCode().getStatus(), e.getMessage(), null);
    }
    
    public static <T> CommonResponse<T> fail(Exception e) {
        return new CommonResponse(500, e.getMessage(), null);
    }
}
