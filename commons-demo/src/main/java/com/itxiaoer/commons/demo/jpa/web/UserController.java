package com.itxiaoer.commons.demo.jpa.web;

import com.itxiaoer.commons.core.page.Response;
import com.itxiaoer.commons.demo.jpa.dto.UserDto;
import com.itxiaoer.commons.demo.jpa.entity.User;
import com.itxiaoer.commons.demo.jpa.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : liuyk
 */
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/users")
    public Response<String> create(@RequestBody UserDto dto) {
        return Response.ok(this.userService.create(dto).getId());
    }

    @PutMapping("/users/{id}")
    public Response<String> update(@PathVariable String id, @RequestBody UserDto dto) {
        return Response.ok(this.userService.update(id, dto).getId());
    }

    @GetMapping("/users/{id}")
    public Response<User> getById(@PathVariable String id) {
        return Response.ok(this.userService.getById(id).orElse(null));
    }

    @GetMapping("/users")
    public Response<List<User>> list() {
        return Response.ok(this.userService.list());
    }
}
