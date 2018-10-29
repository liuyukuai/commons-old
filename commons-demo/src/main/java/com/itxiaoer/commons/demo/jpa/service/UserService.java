package com.itxiaoer.commons.demo.jpa.service;

import com.itxiaoer.commons.demo.jpa.dto.UserDto;
import com.itxiaoer.commons.demo.jpa.entity.User;
import com.itxiaoer.commons.demo.jpa.repository.UserRepository;
import com.itxiaoer.commons.jpa.service.BasicJpaService;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * @author : liuyk
 */
@Service
public class UserService extends BasicJpaService<UserDto, User, String, UserRepository> {

    @Override
    public BiConsumer<User, UserDto> consumer() {
        return (dest, source) -> dest.setId(UUID.randomUUID().toString());
    }
}
