package com.itxiaoer.commons.demo;

import com.itxiaoer.commons.demo.dto.UserDto;
import com.itxiaoer.commons.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
    private static volatile AtomicInteger create = new AtomicInteger(0);
    private static volatile AtomicInteger update = new AtomicInteger(0);
    private static volatile AtomicInteger get = new AtomicInteger(0);

    private UserDto createDto;

    private UserDto updateDto;

    private User createUser;

    private User updateUser;

    @Autowired
    private MockUserService userService;


    @Before
    public void before() {
        this.createDto = new UserDto();
        this.updateDto = new UserDto();
        this.createUser = new User();
        this.updateUser = new User();
    }


    @Test
    @Repeat(20)
    public void create() throws Exception {
        int i = create.incrementAndGet();
        createDto.setName("张三" + i);
        createDto.setAddress("北京" + i);
        createDto.setAge(i);

        // 测试 userService
        User user = this.userService.create(this.createDto);
        createUser.setId(i + "");
        createUser.setName("张三" + i);
        createUser.setAddress("北京" + i);
        createUser.setAge(i);
        Assert.assertEquals(user, createUser);
    }

    @Test
    @Repeat(20)
    public void update() throws Exception {
        int i = update.incrementAndGet();
        int num = i + 1;
        updateDto.setName("张三" + num);
        updateDto.setAddress("北京" + num);
        updateDto.setAge(num);

        User updateUser = this.userService.update(i + "", this.updateDto);
        updateUser.setId(i + "");
        updateUser.setName("张三" + num);
        updateUser.setAddress("北京" + num);
        updateUser.setAge(num);

        Assert.assertEquals(updateUser, updateUser);
    }


    @Test
    @Repeat(20)
    public void getById() throws Exception {
        int i = get.incrementAndGet();
        int num = i + 1;
        Optional<User> byId = this.userService.getById(i + "");

        updateUser.setId(i + "");
        updateUser.setName("张三" + num);
        updateUser.setAddress("北京" + num);
        updateUser.setAge(num);

        Assert.assertEquals(byId.orElse(null), updateUser);

    }


    @Test
    public void listEq() throws Exception {
        for (int i = 1; i < 20; i++) {
            List<User> users = this.userService.listByWhere(new UserQueryEq(String.valueOf(i)));
            int num = i + 1;
            updateUser.setId(String.valueOf(i));
            updateUser.setName("张三" + num);
            updateUser.setAddress("北京" + num);
            updateUser.setAge(num);
            Assert.assertEquals(users.get(0), updateUser);

        }

    }


    @Test
    @Repeat(20)
    public void listLike() throws Exception {
        List<User> users = this.userService.listByWhere(new UserQueryLike("张三"));
        Assert.assertEquals(users.size(), 20);
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class UserQueryEq {
        private String id;

    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class UserQueryLike {
        private String name;
    }
}
