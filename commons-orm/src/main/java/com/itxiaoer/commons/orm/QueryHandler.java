package com.itxiaoer.commons.orm;

import com.itxiaoer.commons.core.Exclude;
import com.itxiaoer.commons.core.Operator;
import com.itxiaoer.commons.core.Transform;
import org.apache.commons.lang3.StringUtils;
import org.joor.Reflect;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 查询对象处理类
 *
 * @author : liuyk
 */
public final class QueryHandler {

    private QueryHandler() {
    }

    public static <T> Map<String, Transformation> fields(T queryable) {
        // 获取所有属性
        Map<String, Reflect> fields = Reflect.on(queryable).fields();
        // 过滤掉排除的属性
        return fields.entrySet().stream().filter(e -> !exclude(queryable.getClass(), e.getKey()))
                // 转换
                .collect(Collectors.toMap(Map.Entry::getKey, e -> QueryHandler.transform(queryable, e.getKey(), e.getValue())));
    }

    private static <T> Transformation transform(T queryable, String name, Reflect value) {
        try {
            Class<?> clazz = queryable.getClass();
            Transform transform = clazz.getDeclaredField(name).getAnnotation(Transform.class);
            // 如果属性没有配置注解，采用默认
            if (transform == null) {
                return new Transformation(name, value.get(), Operator.EQ, true);
            }
            // 是否有配置属性，没有取字段名称
            name = StringUtils.isBlank(transform.value()) ? name : transform.value();
            return new Transformation(name, value.get(), transform.operator(), transform.ignoreEmpty());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }


    private static <T> boolean exclude(Class<T> clazz, String name) {
        try {
            Exclude annotation = clazz.getDeclaredField(name).getAnnotation(Exclude.class);
            return annotation != null;
        } catch (NoSuchFieldException e) {
            // ignore
            return true;
        }
    }
}
