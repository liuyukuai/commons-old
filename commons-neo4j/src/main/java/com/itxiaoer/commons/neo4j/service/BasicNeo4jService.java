package com.itxiaoer.commons.neo4j.service;

import com.itxiaoer.commons.core.NotFoundException;
import com.itxiaoer.commons.core.page.PageResponse;
import com.itxiaoer.commons.core.page.Paging;
import com.itxiaoer.commons.core.page.Sort;
import com.itxiaoer.commons.core.util.Lists;
import com.itxiaoer.commons.neo4j.BasicRelation;
import com.itxiaoer.commons.neo4j.repository.Neo4jRelationRepository;
import com.itxiaoer.commons.orm.page.PagingUtils;
import com.itxiaoer.commons.orm.service.BasicService;
import com.itxiaoer.commons.orm.validate.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.util.IterableUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author : liuyk
 */
@SuppressWarnings("all")
public abstract class BasicNeo4jService<DTO, E, ID extends Serializable, JPA extends Neo4jRepository<E, ID>> implements BasicService<DTO, E, ID>, Validate<DTO, ID>, Neo4jRelationService {

    @Autowired
    private JPA repository;

    @Autowired
    private Neo4jRelationRepository neo4jRelationRepository;

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
        this.repository.save(process);
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
        Optional<E> optional = this.repository.findById(id);
        E e = optional.orElseThrow(NotFoundException::new);
        this.process(e, dto, consumer);
        this.repository.save(e);
        return e;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public E update(ID id, Consumer<E> consumer) {
        this.idValid(id);
        Optional<E> optional = this.repository.findById(id);
        E e = optional.orElseThrow(NotFoundException::new);
        consumer.accept(e);
        return this.repository.save(e);
    }


    @Transactional(rollbackFor = Exception.class)
    public E update(E e) {
        return this.repository.save(e);
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
            Iterable<E> list = this.repository.findAllById(ids);
            return IterableUtils.toList(list);
        }
        return Lists.newArrayList();
    }

    @Override
    public List<E> list() {
        return IterableUtils.toList(this.repository.findAll());
    }

    @Override
    public List<E> list(Sort... sorts) {
        return IterableUtils.toList(this.repository.findAll(PagingUtils.of(sorts)));
    }

    @Override
    public PageResponse<E> list(Paging paging) {
        Page<E> page = this.repository.findAll(PagingUtils.of(paging));
        return PagingUtils.of(page);
    }

    /**
     * 获取数据层对象
     *
     * @return 数据持久层对象
     */
    public JPA getRepository() {
        return this.repository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T extends BasicRelation> T create(T basicRelation) {
        this.neo4jRelationRepository.save(basicRelation);
        return basicRelation;
    }

}
