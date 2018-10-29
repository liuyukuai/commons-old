package com.itxiaoer.commons.demo.neo4j.entity;

import com.itxiaoer.commons.neo4j.BasicRelation;
import lombok.Data;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * @author : liuyk
 */
@Data
@RelationshipEntity(type = "down")
public class DownRelation extends BasicRelation {
    private static String name = "down";

    public DownRelation() {
        super(name);
    }

    public DownRelation(Classify source, Classify target) {
        super(name, source, target);
    }

    public static DownRelation of(Classify source, Classify target) {
        return new DownRelation(source, target);
    }

}
