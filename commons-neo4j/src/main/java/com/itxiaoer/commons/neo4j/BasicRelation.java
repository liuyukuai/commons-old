package com.itxiaoer.commons.neo4j;

import lombok.Data;
import org.neo4j.ogm.annotation.*;

import java.time.LocalDateTime;

/**
 * neo4j关系
 *
 * @author : liuyk
 */
@Data
@SuppressWarnings("unused")
@RelationshipEntity(type = "DEFAULT")
public class BasicRelation {
    /**
     * 关系id
     */
    @Id
    @GeneratedValue
    private Long id;
    /**
     * 关系起始节点
     */
    @StartNode
    private BasicNode source;


    /**
     * 关系目标节点
     */
    @EndNode
    private BasicNode target;

    /**
     * 关系名称
     */
    @Property
    private String name;

    /**
     * 节点创建时间
     */
    @Property
    private LocalDateTime createTime;

    public BasicRelation() {
        this.createTime = LocalDateTime.now();
    }

    public BasicRelation(String name) {
        this();
        this.name = name;
    }

    public BasicRelation(String name, BasicNode source, BasicNode target) {
        this(name);
        this.target = target;
        this.source = source;
    }
}
