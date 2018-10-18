package com.itxiaoer.commons.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 属性名称转换器
 *
 * @author : liuyk
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Transform {
    /**
     * 对应数据库字段名称，默认和属性名一致
     *
     * @return 字段名称
     */
    String[] value() default {};

    /**
     * 操作方式 默认采用 相等（Operator.EQ）
     *
     * @return 操作方式
     */
    Operator operator() default Operator.EQ;


    /**
     * 多个属性间关系，默认为OR， Operator.OR |  Operator.AND
     *
     * @return 属性关系
     */
    Operator relation() default Operator.OR;

    /**
     * 是否忽略空值，默认忽略
     *
     * @return true|false
     */
    boolean ignoreEmpty() default true;
}
