package com.itxiaoer.commons.demo.service;

import com.itxiaoer.commons.demo.neo4j.dto.ClassifyDto;
import com.itxiaoer.commons.demo.neo4j.entity.Classify;
import com.itxiaoer.commons.demo.neo4j.repository.ClassifyRepository;
import com.itxiaoer.commons.neo4j.service.BasicNeo4jService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;

@Service
@SuppressWarnings("WeakerAccess")
@Transactional(rollbackFor = Exception.class)
public class MockClassifyService extends BasicNeo4jService<ClassifyDto, Classify, String, ClassifyRepository> {
    private static volatile AtomicLong i = new AtomicLong(0);

    @Override
    public BiConsumer<Classify, ClassifyDto> consumer() {
        return (dest, source) -> {
//            dest.setId(i.incrementAndGet());
            dest.setCreateTime(LocalDateTime.now());
        };

    }
}
