package com.iko.restapi.common.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class PaginationResponse<T> extends CommonResponse<List<T>> {
    private final Meta meta;

    private PaginationResponse(Meta meta, List<T> data) {
        super(200, null, "ok", data);
        this.meta = meta;
    }

    public static <T> PaginationResponse<T> success(int page, String orderBy, List<T> data) {
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
        @ApiModelProperty(example = "0")
        private int page;
        @ApiModelProperty(example = "20")
        private int size;
        @ApiModelProperty(example = "date_desc")
        private String orderBy;
    }
}
