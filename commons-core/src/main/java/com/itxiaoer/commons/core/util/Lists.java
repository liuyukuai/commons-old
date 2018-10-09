package com.itxiaoer.commons.core.util;

import com.itxiaoer.commons.core.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * list utils
 *
 * @author : liuyk
 */
@SuppressWarnings("unused")
public final class Lists {

    /**
     * create arrayList
     *
     * @param <E> element type
     * @return arrayList
     */
    public static <E> List<E> newArrayList() {
        return new ArrayList<>();
    }

    /**
     * list is iterable
     *
     * @param list list
     * @param <E>  element type
     * @return true | false
     */
    public static <E> boolean iterable(List<E> list) {
        return list != null && !list.isEmpty();
    }

    /**
     * null to empty list
     *
     * @param list list
     * @param <E>  element type
     * @return list
     */
    public static <E> List<E> empty(List<E> list) {
        return Optional.ofNullable(list).orElse(newArrayList());
    }

    /**
     * clean null element
     *
     * @param list list
     * @param <E>  element type
     * @return list
     */
    public static <E> List<E> clean(List<E> list) {
        if (iterable(list)) {
            return list.stream().filter(e -> {
                if (Objects.isNull(e)) {
                    return false;
                }
                if (e instanceof Nullable) {
                    return !((Nullable) e).isNull();
                }
                return true;
            }).collect(Collectors.toList());
        }
        return list;
    }

}
