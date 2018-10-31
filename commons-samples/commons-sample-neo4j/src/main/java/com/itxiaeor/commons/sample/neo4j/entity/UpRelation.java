package com.itxiaeor.commons.sample.neo4j.entity;

import com.itxiaoer.commons.neo4j.BasicRelation;
import org.neo4j.ogm.annotation.RelationshipEntity;

/**
 * @author : liuyk
 */
@SuppressWarnings("unused")
@RelationshipEntity(type = "up")
public class UpRelation extends BasicRelation {
    private static String name = "up";

    public UpRelation() {
        super(name);
    }

    private UpRelation(Classify source, Classify target) {
        super(name, source, target);
    }

    public static UpRelation of(Classify source, Classify target) {
        return new UpRelation(source, target);
    }

}
