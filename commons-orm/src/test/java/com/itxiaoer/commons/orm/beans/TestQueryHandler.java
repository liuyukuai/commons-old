package com.itxiaoer.commons.orm.beans;

import com.itxiaoer.commons.core.Operator;
import com.itxiaoer.commons.orm.QueryHandler;
import com.itxiaoer.commons.orm.Queryable;
import com.itxiaoer.commons.orm.Transformation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * 测试QueryHandler
 *
 * @author : liuyk
 */
public class TestQueryHandler {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class UserQuery implements Queryable {
        private String name;
        private String id;
        private String exclude;
        private String transform;
        private String transform1;
        private String transform2;
        private String transform3;

        private String transform4;
        private String transform5;
    }


    private UserQuery nullable;
    private UserQuery empty;
    private UserQuery userQuery;


    @Before
    public void before() {

        this.nullable = null;
        // 空对象
        this.empty = new UserQuery();

        // 部分属性
        this.userQuery = UserQuery.builder().exclude("exclude").id("id")
                .name("name").transform("transform")
                .transform1("transform1").transform2("transform2")
                .transform3("transform3").transform4("transform4").build();


    }

    @Test
    public void transform() {

        // null
        Map<String, Transformation> fields = QueryHandler.fields(this.nullable);
        Assert.assertEquals(fields.size(), 0);

        // empty
        fields = QueryHandler.fields(this.empty);
        Assert.assertEquals(fields.size(), 8);

        // userQuery
        fields = QueryHandler.fields(this.userQuery);
        Assert.assertEquals(fields.size(), 8);

        // 判断属性的值
        Assert.assertEquals(new Transformation("exclude", "exclude", Operator.EQ, true).toString(), fields.get("exclude").toString());
        Assert.assertEquals(new Transformation("id", "id", Operator.EQ, true).toString(), fields.get("id").toString());
        Assert.assertEquals(new Transformation("name", "name", Operator.EQ, true).toString(), fields.get("name").toString());
        Assert.assertEquals(new Transformation("transform", "transform", Operator.EQ, true).toString(), fields.get("transform").toString());
        Assert.assertEquals(new Transformation("transform_1", "transform1", Operator.EQ, true).toString(), fields.get("transform1").toString());
        Assert.assertEquals(new Transformation("transform_2", "transform2", Operator.IN, true).toString(), fields.get("transform2").toString());
        Assert.assertEquals(new Transformation("transform3", "transform3", Operator.EQ, true).toString(), fields.get("transform3").toString());
        Assert.assertEquals(new Transformation("transform4", "transform4", Operator.EQ, false).toString(), fields.get("transform4").toString());


    }


}
