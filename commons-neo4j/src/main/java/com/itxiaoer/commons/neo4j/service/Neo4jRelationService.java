package com.itxiaoer.commons.neo4j.service;

import com.itxiaoer.commons.neo4j.BasicRelation;

/**
 * @author : liuyk
 */
@SuppressWarnings("all")
public interface Neo4jRelationService {

    /**
     * 创建节点关系
     *
     * @param t 具体关系
     * @return 关系id
     */
    <T extends BasicRelation> T create(T t);

}
