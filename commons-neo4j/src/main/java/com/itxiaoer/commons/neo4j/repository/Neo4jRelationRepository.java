package com.itxiaoer.commons.neo4j.repository;

import com.itxiaoer.commons.neo4j.BasicRelation;
import org.springframework.data.neo4j.repository.Neo4jRepository;

/**
 * @author : liuyk
 */
public interface Neo4jRelationRepository extends Neo4jRepository<BasicRelation, String> {
}
