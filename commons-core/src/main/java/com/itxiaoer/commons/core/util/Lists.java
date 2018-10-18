package com.itxiaoer.commons.core.util;

import com.itxiaoer.commons.core.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.*;
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
     * list is iterable
     *
     * @param array list
     * @param <E>   element type
     * @return true | false
     */
    public static <E> boolean iterable(E[] array) {
        return array != null && Array.getLength(array) != 0;
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
