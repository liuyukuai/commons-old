package com.itxiaoer.commons.core;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindFieldTest {

    static class Parent {
        private String id;
    }

    static class Sun extends Parent {
        private String name;
    }

    @Test
    public void find() {
        Class cls = Sun.class;
        List<Field> fields = new ArrayList<>();
        do {
            Field[] sFields = cls.getDeclaredFields();
            fields.addAll(Arrays.asList(sFields));
            cls = cls.getSuperclass();
        } while (cls != null);
        Assert.assertEquals(2, fields.size());
    }
}
