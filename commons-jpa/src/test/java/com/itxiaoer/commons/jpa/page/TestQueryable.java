package com.itxiaoer.commons.jpa.page;

import org.junit.Before;
import org.junit.Test;

/**
 * 测试参数校验
 *
 * @author : liuyk
 */
public class TestQueryable {
    private UserQuery userQuery;

    @Before
    public void before() {
        userQuery = new UserQuery("name", "id");
    }

    @Test
    public void query(){

    }


}
