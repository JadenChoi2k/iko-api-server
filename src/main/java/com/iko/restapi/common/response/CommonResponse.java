package com.iko.restapi.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iko.restapi.common.exception.BaseException;
import lombok.Data;

@Data
public class CommonResponse {
    private String code;
    private String msg;
    private Object data;

    protected CommonResponse(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static CommonResponse ok() {
        return new CommonResponse("200", "ok", null);
    }

    public static CommonResponse ok(String msg) {
        return new CommonResponse("200", msg, null);
    }

    public static CommonResponse success(Object data) {
        return new CommonResponse("200", "ok", data);
    }

    public static CommonResponse fail(BaseException e) {
        return new CommonResponse(e.getErrorCode().name(), e.getMessage(), null);
    }
}
