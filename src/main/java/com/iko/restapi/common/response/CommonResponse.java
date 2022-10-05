package com.iko.restapi.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.iko.restapi.common.exception.BaseException;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CommonResponse {
    private int code;
    private String msg;
    private Object data;

    protected CommonResponse(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static CommonResponse ok() {
        return new CommonResponse(200, "ok", null);
    }

    public static CommonResponse ok(String msg) {
        return new CommonResponse(200, msg, null);
    }

    public static CommonResponse success(Object data) {
        return new CommonResponse(200, "ok", data);
    }

    public static CommonResponse fail(BaseException e) {
        return new CommonResponse(e.getErrorCode().getStatus(), e.getMessage(), null);
    }
}
