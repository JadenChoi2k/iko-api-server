package com.iko.restapi.common.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

public class PaginationResponse extends CommonResponse {
    private Meta meta;

    private PaginationResponse(Meta meta, Object data) {
        super(null, null, data);
        this.meta = meta;
    }

    public static CommonResponse success(int page, String orderBy, List<?> data) {
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
