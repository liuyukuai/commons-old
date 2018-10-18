package com.itxiaoer.commons.jpa;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 查询条件容器
 *
 * @author : liuyk
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Criteria<T> implements Specification<T> {

    private List<Criterion> criteria = new ArrayList<>();

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (!criteria.isEmpty()) {
            List<Predicate> predicates = new ArrayList<>();
            for (Criterion c : criteria) {
                predicates.add(c.toPredicate(root, criteriaQuery, criteriaBuilder));
            }
            if (predicates.size() > 0) {
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        }
        return criteriaBuilder.conjunction();
    }

    /**
     * 添加表达式
     *
     * @param criterion 表达式
     */
    public void add(Criterion criterion) {
        if (!Objects.isNull(criterion)) {
            criteria.add(criterion);
        }
    }
}
