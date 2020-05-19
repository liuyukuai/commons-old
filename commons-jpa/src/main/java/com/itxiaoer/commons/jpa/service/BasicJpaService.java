package com.itxiaoer.commons.jpa.service;


import com.itxiaoer.commons.core.NotFoundException;
import com.itxiaoer.commons.core.page.PageResponse;
import com.itxiaoer.commons.core.page.Paging;
import com.itxiaoer.commons.core.page.Sort;
import com.itxiaoer.commons.core.util.Lists;
import com.itxiaoer.commons.jpa.Restrictions;
import com.itxiaoer.commons.orm.page.PagingUtils;
import com.itxiaoer.commons.orm.service.BasicService;
import com.itxiaoer.commons.orm.service.BasicSpecificationExecutor;
import com.itxiaoer.commons.orm.validate.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author : liuyk
 */

@SuppressWarnings({"all"})
@Transactional(readOnly = true, rollbackFor = Exception.class)
public abstract class BasicJpaService<DTO, E, ID extends Serializable, JPA extends JpaRepository<E, ID> & JpaSpecificationExecutor> implements BasicService<DTO, E, ID>, BasicSpecificationExecutor<E>, Validate<DTO, ID> {

    @Autowired
    private JPA repository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public E create(DTO dto) {
        return this.create(dto, consumer());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public E create(DTO dto, BiConsumer<E, DTO> consumer) {
        this.valid(dto);
        E process = process(dto, consumer);
        this.repository.saveAndFlush(process);
        return process;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public E update(ID id, DTO dto) {
        return this.update(id, dto, (dest, source) -> {
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public E update(ID id, DTO dto, BiConsumer<E, DTO> consumer) {
        this.idValid(id);
        this.valid(id, dto);
        Optional<E> optional = this.repository.findById(id);
        E e = optional.orElseThrow(NotFoundException::new);
        this.process(e, dto, consumer);
        this.repository.saveAndFlush(e);
        return e;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public E update(ID id, Consumer<E> consumer) {
        this.idValid(id);

        Optional<E> optional = this.repository.findById(id);
        E e = optional.orElseThrow(NotFoundException::new);
        consumer.accept(e);
        return this.repository.saveAndFlush(e);
    }


    @Transactional(rollbackFor = Exception.class)
    public E update(E e) {
        return this.repository.saveAndFlush(e);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ID id) {
        this.idValid(id);
        this.repository.deleteById(id);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<ID> ids) {
        if (Lists.iterable(ids)) {
            for (ID id : ids) {
                this.repository.deleteById(id);
            }
        }
    }

    @Override
    public Optional<E> getById(ID id) {
        this.idValid(id);
        return this.repository.findById(id);
    }

    @Override
    public List<E> getById(List<ID> ids) {
        if (Lists.iterable(ids)) {
            return this.repository.findAllById(ids);
        }
        return Lists.newArrayList();
    }

    @Override
    public List<E> list() {
        return this.repository.findAll();
    }

    @Override
    public List<E> list(Sort... sorts) {
        return this.repository.findAll(PagingUtils.of(sorts));
    }

    @Override
    public PageResponse<E> list(Paging paging) {
        Page<E> page = this.repository.findAll(PagingUtils.of(paging));
        return PagingUtils.of(page);
    }

    @Override
    public <T> List<E> listByWhere(T query) {
        return this.repository.findAll(Restrictions.of().where(query).get());
    }

    @Override
    public <T> List<E> listByWhere(T query, Sort... sorts) {
        return this.repository.findAll(Restrictions.of().where(query).get(), PagingUtils.of(sorts));
    }

    @Override
    public <T extends Paging> PageResponse<E> listByWhere(T query) {
        Page<E> page = this.repository.findAll(Restrictions.of().where(query).get(), PagingUtils.of(query));
        return PagingUtils.of(page);
    }

    @Override
    public <T> PageResponse<E> listByWhere(T query, Paging paging) {
        Page<E> page = this.repository.findAll(Restrictions.of().where(query).get(), PagingUtils.of(paging));
        return PagingUtils.of(page);
    }


    @Override
    public Class<E> getGenericClass(int index) {
        Class cls = this.getClass();
        while (!Objects.equals(cls.getSuperclass(), BasicJpaService.class)) {
            cls = cls.getSuperclass();
        }
        ParameterizedType genericInterfaces = (ParameterizedType) cls.getGenericSuperclass();
        return (Class<E>) genericInterfaces.getActualTypeArguments()[index];
    }

    /**
     * 获取数据层对象
     *
     * @return 数据持久层对象
     */
    public JPA getRepository() {
        return this.repository;
    }
}