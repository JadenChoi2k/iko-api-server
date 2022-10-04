package com.iko.restapi.common.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class PaginationResponse extends CommonResponse {
    private Meta meta;

    private PaginationResponse(Meta meta, Object data) {
        super("200", "ok", data);
        this.meta = meta;
    }

    public static PaginationResponse success(int page, String orderBy, List<?> data) {
        return new PaginationResponse(
                Meta.builder()
                        .page(page)
                        .size(data.size())
                        .orderBy(orderBy)
                        .build(),
                data
        );
    }

    @Data
    @Builder
    static class Meta {
        private int page;
        private int size;
        private String orderBy;
    }
}
