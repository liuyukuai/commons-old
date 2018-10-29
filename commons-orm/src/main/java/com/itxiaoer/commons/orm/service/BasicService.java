package com.itxiaoer.commons.orm.service;


import com.itxiaoer.commons.core.beans.ProcessUtils;
import com.itxiaoer.commons.core.page.PageResponse;
import com.itxiaoer.commons.core.page.Paging;
import com.itxiaoer.commons.core.page.Sort;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author : liuyk
 */
@SuppressWarnings("unused")
public interface BasicService<DTO, E, ID> {

    /**
     * 创建对象
     *
     * @param dto dto
     * @return id
     */
    E create(DTO dto);


    /**
     * 创建对象
     *
     * @param dto      dto
     * @param consumer 对象转换器
     * @return id
     */
    E create(DTO dto, BiConsumer<E, DTO> consumer);


    /**
     * 更新对象
     *
     * @param id  id
     * @param dto dto对象
     * @return 更新后的对象
     */
    E update(ID id, DTO dto);


    /**
     * 更新对象
     *
     * @param id       id
     * @param consumer 消费方法
     * @return 更新后的对象
     */
    E update(ID id, Consumer<E> consumer);


    /**
     * 更新对象
     *
     * @param id       id
     * @param dto      dto对象
     * @param consumer 对象转换器
     * @return 更新后的对象
     */
    E update(ID id, DTO dto, BiConsumer<E, DTO> consumer);


    /**
     * 通过id删除对象
     *
     * @param id id
     */
    void delete(ID id);

    /**
     * 通过id删除对象（批量）
     *
     * @param ids ids
     */
    void delete(List<ID> ids);


    /**
     * 通过id查询对象
     *
     * @param id id
     * @return e
     */
    Optional<E> getById(ID id);

    /**
     * 通过id查询对象（批量）
     *
     * @param ids ids
     * @return list
     */
    List<E> getById(List<ID> ids);

    /**
     * 查询所有的对象
     *
     * @return list
     */
    List<E> list();

    /**
     * 查询所有的对象
     *
     * @param sorts 排序规则
     * @return list
     */
    List<E> list(Sort... sorts);


    /**
     * 查询所有的对象（分页）
     *
     * @param paging 分页对象
     * @return list
     */
    PageResponse<E> list(Paging paging);


    /**
     * dto convert to e
     *
     * @param dto dto
     * @return e
     */
    default E process(DTO dto) {
        return process(dto, consumer());
    }

    /**
     * dto convert to e
     *
     * @param dto      dto
     * @param consumer process
     * @return e
     */

    default E process(DTO dto, BiConsumer<E, DTO> consumer) {
        Class<E> clazz = this.getGenericClass(1);
        return ProcessUtils.process(clazz, dto, consumer);
    }


    /**
     * 转换对象
     *
     * @param e        目标对象
     * @param dto      原对象
     * @param consumer 额外转换器
     */
    default void process(E e, DTO dto, BiConsumer<E, DTO> consumer) {
        ProcessUtils.processObject(e, dto, consumer);
    }

    /**
     * 创建对象转换器（只能用于新增）
     *
     * @return 返回对象转换器
     */
    default BiConsumer<E, DTO> consumer() {
        return (dest, source) -> {
        };
    }

    /**
     * get class
     *
     * @param index index
     * @return class
     */
    @SuppressWarnings("unchecked")
    default Class<E> getGenericClass(int index) {
        ParameterizedType genericInterfaces = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<E>) genericInterfaces.getActualTypeArguments()[index];
    }
}
