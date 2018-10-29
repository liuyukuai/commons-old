package com.itxiaoer.commons.demo.neo4j.service;

import com.itxiaoer.commons.demo.neo4j.dto.ClassifyDto;
import com.itxiaoer.commons.demo.neo4j.entity.Classify;
import com.itxiaoer.commons.demo.neo4j.repository.ClassifyRepository;
import com.itxiaoer.commons.neo4j.service.BasicNeo4jService;
import org.springframework.stereotype.Service;

/**
 * @author : liuyk
 */
@Service
public class ClassifyService extends BasicNeo4jService<ClassifyDto, Classify, String, ClassifyRepository> {
}
