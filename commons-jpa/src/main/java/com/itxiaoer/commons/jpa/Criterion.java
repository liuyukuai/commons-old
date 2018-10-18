package com.itxiaoer.commons.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 条件表达式接口
 *
 * @author : liuyk
 */
public interface Criterion {
    /**
     * 转换
     *
     * @param root    root
     * @param query   query
     * @param builder builder
     * @return predicate
     */
    Predicate toPredicate(Root<?> root, CriteriaQuery<?> query,
                          CriteriaBuilder builder);

}
