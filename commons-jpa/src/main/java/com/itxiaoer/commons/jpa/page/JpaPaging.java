package com.itxiaoer.commons.jpa.page;

import com.itxiaoer.commons.core.page.PageResponse;
import com.itxiaoer.commons.core.page.Paging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : liuyk
 */
@SuppressWarnings({"WeakerAccess"})
public class JpaPaging {

    private static JpaPageFunction defaultFunction = (sorts -> {
        if (sorts.isEmpty()) {
            return Sort.unsorted();
        }
        List<Sort.Order> orders = sorts.stream().map(sort -> new org.springframework.data.domain.Sort.Order(org.springframework.data.domain.Sort.Direction.fromString(sort.getDirection()), sort.getName())).collect(Collectors.toList());
        return org.springframework.data.domain.Sort.by(orders);
    });

    public static PageRequest of(Paging paging) {
        return of(paging, defaultFunction);
    }


    public static PageRequest of(Paging paging, JpaPageFunction jpaPageFunction) {
        return paging.get(jpaPageFunction);
    }

    public static <E> PageResponse<E> of(Page<E> page) {
        return PageResponse.apply(page.getTotalElements(), page.getContent());
    }
}
