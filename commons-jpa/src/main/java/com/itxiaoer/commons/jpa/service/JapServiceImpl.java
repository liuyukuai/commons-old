package com.itxiaoer.commons.jpa.service;


import com.itxiaoer.commons.core.NotFoundException;
import com.itxiaoer.commons.core.beans.ProcessUtils;
import com.itxiaoer.commons.core.page.PageResponse;
import com.itxiaoer.commons.core.page.Paging;
import com.itxiaoer.commons.core.page.Response;
import com.itxiaoer.commons.core.util.Lists;
import com.itxiaoer.commons.jpa.page.JpaPaging;
import com.itxiaoer.commons.orm.service.BasicService;
import com.itxiaoer.commons.orm.validate.Validate;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * @author : liuyk
 */

@SuppressWarnings("unused")
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class JapServiceImpl<DTO, E, ID extends Serializable, JPA extends JpaRepository<E, ID>> implements BasicService<DTO, E, ID>, Validate<DTO, ID> {

    @Resource
    private JPA repository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<E> create(DTO dto) {
        return this.create(dto, consumer());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<E> create(DTO dto, BiConsumer<E, DTO> consumer) {
        this.valid(dto);
        E process = process(dto, consumer);
        this.repository.saveAndFlush(process);
        return Response.ok(process);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<E> update(ID id, DTO dto) {
        return this.update(id, dto, consumer());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<E> update(ID id, DTO dto, BiConsumer<E, DTO> consumer) {
        this.idValid(id);
        Optional<E> optional = this.repository.findById(id);
        E e = optional.orElseThrow(NotFoundException::new);
        ProcessUtils.processObject(e, dto, consumer);
        this.repository.saveAndFlush(e);
        return Response.ok(e);
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
        return this.repository.findAllById(ids);
    }

    @Override
    public List<E> list() {
        return this.repository.findAll();
    }

    @Override
    public PageResponse<E> list(Paging paging) {
        Page<E> page = this.repository.findAll(JpaPaging.of(paging));
        return JpaPaging.of(page);
    }
}
