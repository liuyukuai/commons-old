package com.itxiaeor.commons.sample.neo4j.entity;

import com.itxiaoer.commons.neo4j.BasicRelation;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * @author : liuyk
 */
@SuppressWarnings("unused")
@RelationshipEntity(type = "down")
public class DownRelation extends BasicRelation {
    private static String name = "down";

    public DownRelation() {
        super(name);
    }

    private DownRelation(Classify source, Classify target) {
        super(name, source, target);
    }

    public static DownRelation of(Classify source, Classify target) {
        return new DownRelation(source, target);
    }

}
