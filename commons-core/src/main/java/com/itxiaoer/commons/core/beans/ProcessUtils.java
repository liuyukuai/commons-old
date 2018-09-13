package com.itxiaoer.commons.core.beans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
     * @return 目标对象集合
     */
    public static <T, R> List<R> processList(Class<R> clazz, List<T> src) {
        return processList(clazz, src, (r, s) -> {
        });
    }

    /**
     * 拷贝list对象
     *
     * @param clazz 目标类型
     * @param src   原对象集合
     * @return 目标对象集合
     */
    public static <T, R> List<R> processList(Class<R> clazz, List<T> src, Callback<R, T> callback) {
        if (src == null || src.isEmpty()) {
            throw new IllegalArgumentException("the src argument must be null");
        }
        List<R> rs = new ArrayList<>();
        for (T t : src) {
            R process = process(clazz, t, callback);
            rs.add(process);
        }
        return rs;
    }

    /**
     * 拷贝单个对象
     *
     * @param clazz 目标类型
     * @param src   原对象
     * @return 目标对象
     */
    public static <T, R> R process(Class<R> clazz, T src) {
        return process(clazz, src, (r, s) -> {
        });
    }

    /**
     * 拷贝单个对象
     *
     * @param clazz    目标类型
     * @param src      原对象
     * @param callback 回调函数
     * @return 目标对象
     */
    public static <T, R> R process(Class<R> clazz, T src, Callback<R, T> callback) {
        if (clazz == null) {
            throw new IllegalArgumentException("the clazz argument must be null");
        }
        if (src == null) {
            throw new IllegalArgumentException("the src argument must be null");
        }

        try {
            R r = clazz.newInstance();
            return processObject(r, src, callback);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static <T, R> R processObject(R r, T src) {
        return processObject(r, src, (r1, src1) -> {
        });
    }

    public static <T, R> R processObject(R r, T src, Callback<R, T> callback) {
        try {
            BeanUtils.copyProperties(src, r);
            String beanKey = generateKey(src.getClass(), r.getClass());
            BeanCopier copier;
            if (!BEAN_COPIER_MAP.containsKey(beanKey)) {
                copier = BeanCopier.create(src.getClass(), r.getClass(), false);
                BEAN_COPIER_MAP.put(beanKey, copier);
            } else {
                copier = BEAN_COPIER_MAP.get(beanKey);
            }
            copier.copy(src, r, null);
            callback.call(r, src);
            return r;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }


    private static String generateKey(Class<?> class1, Class<?> class2) {
        return String.join(class1.toString(), class2.toString());
    }

    /**
     * callback
     *
     * @author liuyk
     */
    @FunctionalInterface
    public interface Callback<R, S> {

        /**
         * 回调函数
         *
         * @param dest 目标对象
         * @param src  原始对象
         */
        void call(R dest, S src);
    }

}
