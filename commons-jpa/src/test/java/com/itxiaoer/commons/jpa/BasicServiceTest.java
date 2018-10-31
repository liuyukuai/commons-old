package com.itxiaoer.commons.jpa;


import com.itxiaoer.commons.jpa.beans.User;
import com.itxiaoer.commons.jpa.beans.UserDto;
import com.itxiaoer.commons.jpa.beans.UserRepository;
import com.itxiaoer.commons.jpa.service.BasicJpaService;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("WeakerAccess")
public class BasicServiceTest {
    static class A extends BasicJpaService<UserDto, User, String, UserRepository> {

    }

    static class B extends A {

    }

    static class C extends B {

    }


    @Test
    public void getGenericClass() {
        Class<User> genericClass = new A().getGenericClass(1);
        Assert.assertEquals(genericClass, User.class);

        genericClass = new B().getGenericClass(1);
        Assert.assertEquals(genericClass, User.class);

        genericClass = new C().getGenericClass(1);
        Assert.assertEquals(genericClass, User.class);

    }

}
