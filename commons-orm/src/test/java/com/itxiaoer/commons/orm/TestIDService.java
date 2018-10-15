package com.itxiaoer.commons.orm;

import com.itxiaoer.commons.orm.beans.User;
import com.itxiaoer.commons.orm.beans.UserDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author : liuyk
 */
public class TestIDService {

    private UserService userService;

    private UserDto userDto;

    private User user;

    @Before
    public void before() {
        this.userService = new UserService();
        userDto = UserDto.builder().boy(false).name("james").build();
        user = new User();

        user.setBoy(false);
        user.setName("james");

    }


    @Test
    public void test() {

        Class genericClass = userService.getGenericClass(1);
        Assert.assertEquals(genericClass, User.class);


        User process = userService.process(userDto);
        Assert.assertEquals(process, user);


        user.setId("1");
        process = userService.process(userDto, (dest, source) -> dest.setId("1"));
        Assert.assertEquals(process, user);

    }

}
