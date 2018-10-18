package com.itxiaoer.commons.demo;

import com.itxiaoer.commons.core.Operator;
import com.itxiaoer.commons.core.Transform;
import com.itxiaoer.commons.demo.dto.UserDto;
import com.itxiaoer.commons.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest
// 方法执行顺序，按照方法名
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Rollback
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
    public void t01_create() {
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
    public void t02_update() {
        int i = update.incrementAndGet();
        int num = i * 2;
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
    public void t03_getById() {
        int i = get.incrementAndGet();
        int num = i * 2;
        Optional<User> byId = this.userService.getById(i + "");

        updateUser.setId(i + "");
        updateUser.setName("张三" + num);
        updateUser.setAddress("北京" + num);
        updateUser.setAge(num);

        Assert.assertEquals(byId.orElse(null), updateUser);

    }


    @Test
    public void t04_listEq() {
        for (int i = 1; i < 20; i++) {
            List<User> users = this.userService.listByWhere(new UserQueryEq(String.valueOf(i)));
            int num = i * 2;
            updateUser.setId(String.valueOf(i));
            updateUser.setName("张三" + num);
            updateUser.setAddress("北京" + num);
            updateUser.setAge(num);
            Assert.assertEquals(users.get(0), updateUser);

        }

    }

    @Test
    public void t05_listNe() {
        List<User> users = this.userService.listByWhere(new UserQueryNe("1"));
        Assert.assertEquals(users.size(), 19);
    }

    @Test
    public void t06_listGt() {
        List<User> users = this.userService.listByWhere(new UserQueryGt(18));
        Assert.assertEquals(users.size(), 11);
    }

    @Test
    public void t07_listGte() {
        List<User> users = this.userService.listByWhere(new UserQueryGte(18));
        Assert.assertEquals(users.size(), 12);
    }

    @Test
    public void t08_listLt() {
        List<User> users = this.userService.listByWhere(new UserQueryLt(18));
        Assert.assertEquals(users.size(), 8);
    }

    @Test
    public void t09_listLte() {
        List<User> users = this.userService.listByWhere(new UserQueryLte(18));
        Assert.assertEquals(users.size(), 9);
    }


    @Test
    public void t10_listIn() {
        List<User> users = this.userService.listByWhere(new UserQueryIn(Arrays.asList("1", "2")));
        Assert.assertEquals(users.size(), 2);
    }


    @Test
    public void t11_listNotIn() {
        List<User> users = this.userService.listByWhere(new UserQueryNotIn(Arrays.asList("1", "2")));
        Assert.assertEquals(users.size(), 18);
    }

    @Test
    @Repeat(20)
    public void t12_listLike() {
        List<User> users = this.userService.listByWhere(new UserQueryLike("张三"));
        Assert.assertEquals(users.size(), 20);
    }


    @Test
    @Repeat(20)
    public void t13_listLikeAndEq() {
        List<User> users = this.userService.listByWhere(new UserQueryEqAndLike("2", "张三"));
        Assert.assertEquals(users.size(), 1);
    }


    @Test
    @Repeat(20)
    public void t14_listLikeAndGte() {
        List<User> users = this.userService.listByWhere(new UserQueryGteAndLike(18, "张三"));
        Assert.assertEquals(users.size(), 12);
    }

//    @After
//    public void delete() {
//        for (int i = 1; i < 20; i++) {
//            this.userService.delete(String.valueOf(i));
//        }
//    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class UserQueryEq {
        private String id;

    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class UserQueryNe {
        @Transform(operator = Operator.NE)
        private String id;

    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class UserQueryGt {
        @Transform(operator = Operator.GT)
        private Integer age;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class UserQueryGte {
        @Transform(operator = Operator.GTE)
        private Integer age;

    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class UserQueryLt {
        @Transform(operator = Operator.LT)
        private Integer age;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class UserQueryLte {
        @Transform(operator = Operator.LTE)
        private Integer age;

    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class UserQueryIn {
        @Transform(value = "id", operator = Operator.IN)
        private List<String> id;

    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class UserQueryNotIn {
        @Transform(operator = Operator.NOT_IN)
        private List<String> id;

    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class UserQueryLike {
        @Transform(operator = Operator.LIKE)
        private String name;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class UserQueryEqAndLike {

        private String id;

        @Transform(operator = Operator.LIKE)
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class UserQueryGteAndLike {

        @Transform(operator = Operator.GTE)
        private Integer age;

        @Transform(operator = Operator.LIKE)
        private String name;
    }
}
