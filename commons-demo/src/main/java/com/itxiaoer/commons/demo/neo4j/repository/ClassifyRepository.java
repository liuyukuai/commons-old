package com.itxiaoer.commons.demo.neo4j.repository;

import com.itxiaoer.commons.demo.neo4j.entity.Classify;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * @author : liuyk
 */
public interface ClassifyRepository extends Neo4jRepository<Classify, String> {
}
