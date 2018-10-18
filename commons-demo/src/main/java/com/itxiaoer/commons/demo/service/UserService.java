package com.itxiaoer.commons.demo.service;

import com.itxiaoer.commons.demo.dto.UserDto;
import com.itxiaoer.commons.demo.entity.User;
import com.itxiaoer.commons.demo.repository.UserRepository;
import com.itxiaoer.commons.jpa.service.BaseJpaService;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * @author : liuyk
 */
@Service
public class UserService extends BaseJpaService<UserDto, User, String, UserRepository> {

    @Override
    public BiConsumer<User, UserDto> consumer() {
        return (dest, source) -> dest.setId(UUID.randomUUID().toString());
    }
}
