package com.itxiaoer.commons.core.beans;

import com.itxiaoer.commons.core.util.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * processUtils
 *
 * @author liuyk
 */
@Slf4j
@SuppressWarnings({"unused", "WeakerAccess"})
public final class ProcessUtils {

    public final static Map<String, BeanCopier> BEAN_COPIER_MAP = new ConcurrentHashMap<>();

    private ProcessUtils() {
    }

    /**
     * 拷贝list对象
     *
     * @param clazz 目标类型
     * @param src   原对象集合
     * @param <T>   原数据类型
     * @param <R>   目标数据类型
     * @return 目标对象集合
     */
    public static <T, R> List<R> processList(Class<R> clazz, List<T> src) {
        return processList(clazz, src, (r, s) -> {
        });
    }

    /**
     * 拷贝list对象
     *
     * @param clazz      目标类型
     * @param src        原对象集合
     * @param biConsumer 回调函数
     * @param <T>        原数据类型
     * @param <R>        目标数据类型
     * @return 目标对象集合
     */
    public static <T, R> List<R> processList(Class<R> clazz, List<T> src, BiConsumer<R, T> biConsumer) {
        if (!Lists.iterable(src)) {
            throw new IllegalArgumentException("the src argument must be null");
        }
        return src.stream().map(e -> process(clazz, e, biConsumer)).collect(Collectors.toList());
    }

    /**
     * 拷贝单个对象
     *
     * @param clazz 目标类型
     * @param src   原对象
     * @param <T>   原数据类型
     * @param <R>   目标数据类型
     * @return 目标对象
     */
    public static <T, R> R process(Class<R> clazz, T src) {
        return process(clazz, src, (r, s) -> {
        });
    }

    /**
     * 拷贝单个对象
     *
     * @param clazz      目标类型
     * @param src        原对象
     * @param biConsumer 回调函数
     * @param <T>        原数据类型
     * @param <R>        目标数据类型
     * @return 目标对象
     */
    public static <T, R> R process(Class<R> clazz, T src, BiConsumer<R, T> biConsumer) {
        if (Objects.isNull(clazz)) {
            throw new IllegalArgumentException("the clazz argument must be null");
        }
        if (Objects.isNull(src)) {
            throw new IllegalArgumentException("the src argument must be null");
        }
        try {
            R r = clazz.newInstance();
            return processObject(r, src, biConsumer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * 拷贝单个对象
     *
     * @param r   目标对象
     * @param src 原对象
     * @param <T> 原数据类型
     * @param <R> 目标数据类型
     * @return 目标对象
     */
    public static <T, R> R processObject(R r, T src) {
        return processObject(r, src, (r1, src1) -> {
        });
    }

    /**
     * 拷贝单个对象
     *
     * @param r          目标对象
     * @param src        原对象
     * @param biConsumer 回调函数
     * @param <T>        原数据类型
     * @param <R>        目标数据类型
     * @return 目标对象
     */

    public static <T, R> R processObject(R r, T src, BiConsumer<R, T> biConsumer) {
        try {
            String beanKey = generateKey(src.getClass(), r.getClass());
            BeanCopier copier;
            if (!BEAN_COPIER_MAP.containsKey(beanKey)) {
                copier = BeanCopier.create(src.getClass(), r.getClass(), false);
                BEAN_COPIER_MAP.put(beanKey, copier);
            } else {
                copier = BEAN_COPIER_MAP.get(beanKey);
            }
            copier.copy(src, r, null);
            if (biConsumer != null) {
                biConsumer.accept(r, src);
            }
            return r;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private static String generateKey(Class<?> class1, Class<?> class2) {
        return String.join(class1.toString(), class2.toString());
    }
}
