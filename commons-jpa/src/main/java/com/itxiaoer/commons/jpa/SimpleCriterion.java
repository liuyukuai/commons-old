package com.itxiaoer.commons.jpa;

import com.itxiaoer.commons.core.Operator;
import com.itxiaoer.commons.core.ParameterException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;
import java.lang.reflect.Array;
import java.util.List;

/**
 * 简单查询条件
 *
 * @author : liuyk
 */
@Data
@SuppressWarnings({"unused", "WeakerAccess"})
public class SimpleCriterion implements Criterion {

    private static final String POINT = ".";

    /**
     * 属性名
     */
    private String name;
    /**
     * 对应值
     */
    private Object value;
    /**
     * 计算符
     */
    private Operator operator;

    protected SimpleCriterion(String name, Object value, Operator operator) {
        this.name = name;
        this.value = value;
        this.operator = operator;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query,
                                 CriteriaBuilder builder) {
        Path expression;

        if (name.contains(POINT)) {
            String[] names = StringUtils.split(name, POINT);
            expression = root.get(names[0]);
            if (names.length > 1) {
                for (int i = 1; i < names.length; i++) {
                    expression = expression.get(names[i]);
                }
            }
        } else {
            expression = root.get(name);
        }

        switch (operator) {
            case EQ:
                return builder.equal(expression, value);
            case NE:
                return builder.notEqual(expression, value);
            case LIKE:
                return builder.like((Expression<String>) expression, "%" + value + "%");
            case LT:
                return builder.lessThan(expression, (Comparable) value);
            case GT:
                return builder.greaterThan(expression, (Comparable) value);
            case LTE:
                return builder.lessThanOrEqualTo(expression, (Comparable) value);
            case GTE:
                return builder.greaterThanOrEqualTo(expression, (Comparable) value);
            case IS_MEMBER:
                return builder.isMember(value, expression);
            case IS_NOT_MEMBER:
                return builder.isNotMember(value, expression);
            case IN:
                return in(builder, expression);
            case NOT_IN:
                return notIn(builder, expression);
            default:
                throw new ParameterException("operator not support.");
        }
    }


    private Predicate notIn(CriteriaBuilder builder, Path<Object> expression) {
        return builder.not(in(builder, expression));
    }


    private Predicate in(CriteriaBuilder builder, Path<Object> expression) {
        CriteriaBuilder.In<Object> in = builder.in(expression);
        if (value.getClass().isArray()) {
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                in.value(Array.get(value, i));
            }
            return in;
        } else if (value instanceof List) {
            List objects = (List) value;
            for (Object object : objects) {
                in.value(object);
            }
            return in;
        }
        return in.value(value);
    }

}
