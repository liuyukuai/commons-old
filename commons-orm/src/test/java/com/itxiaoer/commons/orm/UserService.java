package com.itxiaoer.commons.orm;

import com.itxiaoer.commons.core.page.PageResponse;
import com.itxiaoer.commons.core.page.Paging;
import com.itxiaoer.commons.core.page.Response;
import com.itxiaoer.commons.orm.beans.User;
import com.itxiaoer.commons.orm.beans.UserDto;
import com.itxiaoer.commons.orm.service.BasicService;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class UserService implements BasicService<UserDto, User, String> {
    @Override
    public Response<User> create(UserDto userDto) {
        return null;
    }

    @Override
    public Response<User> create(UserDto userDto, BiConsumer<User, UserDto> consumer) {
        return null;
    }

    @Override
    public Response<User> update(String s, UserDto userDto) {
        return null;
    }

    @Override
    public Response<User> update(String s, UserDto userDto, BiConsumer<User, UserDto> consumer) {
        return null;
    }

    @Override
    public void delete(String s) {

    }

    @Override
    public void delete(List<String> strings) {

    }

    @Override
    public Optional<User> getById(String s) {
        return Optional.empty();
    }

    @Override
    public List<User> getById(List<String> strings) {
        return null;
    }

    @Override
    public List<User> list() {
        return null;
    }

    @Override
    public PageResponse<User> list(Paging paging) {
        return null;
    }
}
