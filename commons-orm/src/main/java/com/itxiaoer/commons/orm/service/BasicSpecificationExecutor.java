package com.itxiaoer.commons.orm.service;

import com.itxiaoer.commons.core.page.PageResponse;
import com.itxiaoer.commons.core.page.Paging;
import com.itxiaoer.commons.core.page.Sort;

import java.util.List;

/**
 * 多条件查询接口
 *
 * @author : liuyk
 */
@SuppressWarnings("unused")
public interface BasicSpecificationExecutor<E> {
    /**
     * 查询所有的对象
     *
     * @param query 多条件查询对象
     * @param <T>   任意查询对象
     * @return list
     */
    <T> List<E> listByWhere(T query);


    /**
     * 查询所有的对象
     *
     * @param query 多条件查询对象
     * @param sorts 排序对象
     * @param <T>   任意查询对象
     * @return list
     */
    <T> List<E> listByWhere(T query, Sort... sorts);

    /**
     * 查询所有的对象
     *
     * @param query 多条件查询对象
     * @param <T>   paging
     * @return list
     */
    <T extends Paging> PageResponse<E> listByWhere(T query);


    /**
     * 查询所有的对象
     *
     * @param query  多条件查询对象
     * @param <T>    query
     * @param paging paging
     * @return list
     */
    <T> PageResponse<E> listByWhere(T query, Paging paging);

}
