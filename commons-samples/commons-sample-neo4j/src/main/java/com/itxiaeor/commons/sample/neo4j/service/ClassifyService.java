package com.itxiaeor.commons.sample.neo4j.service;

import com.itxiaeor.commons.sample.neo4j.dto.ClassifyDto;
import com.itxiaeor.commons.sample.neo4j.entity.Classify;
import com.itxiaeor.commons.sample.neo4j.repository.ClassifyRepository;
import com.itxiaoer.commons.neo4j.service.BasicNeo4jService;
import org.springframework.stereotype.Service;

/**
 * @author : liuyk
 */
@Service
public class ClassifyService extends BasicNeo4jService<ClassifyDto, Classify, String, ClassifyRepository> {
}
