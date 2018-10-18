package com.itxiaoer.commons.jpa;

import com.itxiaoer.commons.core.Operator;
import com.itxiaoer.commons.orm.QueryHandler;
import com.itxiaoer.commons.orm.Transformation;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author : liuyk
 */
@SuppressWarnings({"unused", "WeakerAccess", "UnusedReturnValue"})
public class Restrictions<T> {

    private Criteria<T> criteria = new Criteria<>();


    private Restrictions() {

    }

    public static Restrictions of() {
        return new Restrictions<>();
    }


    public <E> Restrictions where(E query) {
        Map<String, Transformation> fields = QueryHandler.fields(query);
        fields.forEach((k, v) -> parse(v));
        return this;
    }

    public Restrictions<T> eq(String name, Object value) {
        parse(name, value, true, Operator.EQ);
        return this;
    }

    public Restrictions<T> eq(String name, Object value, boolean ignoreEmpty) {
        parse(name, value, ignoreEmpty, Operator.EQ);
        return this;
    }


    public Restrictions<T> hasMember(String name, Object value) {
        parse(name, value, true, Operator.IS_MEMBER);
        return this;
    }


    public Restrictions<T> hasMember(String name, Object value, boolean ignoreEmpty) {
        parse(name, value, ignoreEmpty, Operator.IS_MEMBER);
        return this;
    }


    public Restrictions<T> ne(String name, Object value) {
        parse(name, value, true, Operator.NE);
        return this;
    }

    public Restrictions<T> ne(String name, Object value, boolean ignoreEmpty) {
        parse(name, value, ignoreEmpty, Operator.NE);
        return this;
    }


    public Restrictions<T> like(String name, String value) {
        parse(name, value, true, Operator.LIKE);
        return this;
    }

    public Restrictions<T> like(String name, String value, boolean ignoreEmpty) {
        parse(name, value, ignoreEmpty, Operator.LIKE);
        return this;
    }


    public Restrictions<T> gt(String name, Object value) {
        parse(name, value, true, Operator.GT);
        return this;
    }


    public Restrictions<T> gt(String name, Object value, boolean ignoreEmpty) {
        parse(name, value, ignoreEmpty, Operator.GT);
        return this;
    }

    public Restrictions<T> lt(String name, Object value) {
        parse(name, value, true, Operator.LT);
        return this;
    }

    public Restrictions<T> lt(String name, Object value, boolean ignoreEmpty) {
        parse(name, value, ignoreEmpty, Operator.LT);
        return this;
    }


    public Restrictions<T> lte(String name, Object value) {
        parse(name, value, true, Operator.GTE);
        return this;
    }


    public Restrictions<T> lte(String name, Object value, boolean ignoreEmpty) {
        parse(name, value, ignoreEmpty, Operator.GTE);
        return this;
    }

    public Restrictions<T> gte(String name, Object value) {
        parse(name, value, true, Operator.LTE);
        return this;
    }


    public Restrictions<T> gte(String name, Object value, boolean ignoreEmpty) {
        parse(name, value, ignoreEmpty, Operator.LTE);
        return this;
    }


    public Restrictions<T> in(String name, Collection value) {
        parse(name, value, true, Operator.IN);
        return this;

    }

    public Restrictions<T> in(String name, Collection value, boolean ignoreEmpty) {
        parse(name, value, ignoreEmpty, Operator.IN);
        return this;

    }


    public Restrictions<T> notIn(String name, Collection value) {
        parse(name, value, true, Operator.NOT_IN);
        return this;

    }

    public Restrictions<T> notIn(String name, Collection value, boolean ignoreEmpty) {
        parse(name, value, ignoreEmpty, Operator.NOT_IN);
        return this;

    }

    public Restrictions<T> and(Criterion... criterion) {
        criteria.add(new LogicalCriterion(Arrays.asList(criterion), Operator.AND));
        return this;
    }


    public Restrictions<T> or(Criterion... criterion) {
        criteria.add(new LogicalCriterion(Arrays.asList(criterion), Operator.OR));
        return this;
    }

    public Criteria<T> get() {
        return this.criteria;
    }


    private static boolean ignore(Object value, boolean ignoreEmpty) {
        if (Objects.isNull(value) && ignoreEmpty) {
            return true;
        }
        if (value instanceof String) {
            String v = (String) value;
            return StringUtils.isBlank(v) && ignoreEmpty;
        }

        if (value instanceof Collection) {
            Collection v = (Collection) value;
            return v.isEmpty() && ignoreEmpty;
        }
        return false;
    }

    private void parse(String name, Object value, boolean ignoreEmpty, Operator operator) {
        if (ignore(value, ignoreEmpty)) {
            return;
        }
        criteria.add(new SimpleCriterion(name, value, operator));
    }


    private void parse(Transformation transformation) {
        if (ignore(transformation.getValue(), transformation.isIgnoreEmpty())) {
            return;
        }
        String[] fields = transformation.getField();
        if (Array.getLength(fields) == 1) {
            criteria.add(new SimpleCriterion(fields[0], transformation.getValue(), transformation.getOperator()));
        } else {
            List<SimpleCriterion> criterion = Stream.of(fields).map(k -> new SimpleCriterion(k, transformation.getValue(), transformation.getOperator())).collect(Collectors.toList());
            Criterion[] criteria = criterion.toArray(new Criterion[]{});
            if (Operator.OR == transformation.getRelation()) {
                or(criteria);
            } else {
                and(criteria);
            }
        }
    }

}
