package com.itxiaoer.commons.core.rest;

import com.itxiaoer.commons.core.util.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * PageResponse
 *
 * @author liuyk
 */
@Data
@SuppressWarnings("unused")
public class PageResponse<T> implements Serializable {

    private static final long serialVersionUID = -1816325636807144628L;

    private Long total;

    private List<T> data;

    private PageResponse() {
    }

    private PageResponse(Long total, List<T> data) {
        this.total = total;
        this.data = Lists.empty(data);
    }

    public static <E> PageResponse<E> empty() {
        return new PageResponse<>(0L, Lists.newArrayList());
    }


    public static <E> PageResponse<E> apply(long total, List<E> data) {
        total = total < 0 ? 0 : total;
        return new PageResponse<>(total, data);
    }
}
