package com.itxiaoer.commons.neo4j;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import java.time.LocalDateTime;

/**
 * neo4j 节点基础属性
 *
 * @author : liuyk
 */
@Data
@NodeEntity
@SuppressWarnings("unused")
public class BasicNode {
    /**
     * 节点id
     */
    @Id
    @GeneratedValue
    private Long id;
    /**
     * 节点名称
     */
    @Property
    private String name;
    /**
     * 节点创建时间
     */
    @Property
    private LocalDateTime createTime;

}
