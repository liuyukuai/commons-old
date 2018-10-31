package com.itxiaeor.commons.sample.neo4j.repository;

import com.itxiaeor.commons.sample.neo4j.entity.Classify;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * @author : liuyk
 */
public interface ClassifyRepository extends Neo4jRepository<Classify, String> {
}
