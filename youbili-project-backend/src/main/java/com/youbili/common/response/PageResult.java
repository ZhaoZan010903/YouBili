package com.youbili.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    private long page;
    private long size;
    private long total;
    private long pages;
    private List<T> records;

    public static <T> PageResult<T> empty(long page, long size) {
        return PageResult.<T>builder()
                .page(page)
                .size(size)
                .total(0)
                .pages(0)
                .records(Collections.emptyList())
                .build();
    }

    public static <T> PageResult<T> of(long page, long size, long total, List<T> records) {
        long pages = size <= 0 ? 0 : (total + size - 1) / size;
        return PageResult.<T>builder()
                .page(page)
                .size(size)
                .total(total)
                .pages(pages)
                .records(records == null ? Collections.emptyList() : records)
                .build();
    }
}
