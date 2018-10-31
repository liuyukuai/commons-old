package com.itxiaoer.commons.sample.jpa.service;

import com.itxiaoer.commons.jpa.service.BasicJpaService;
import com.itxiaoer.commons.sample.jpa.dto.UserDto;
import com.itxiaoer.commons.sample.jpa.entity.User;
import com.itxiaoer.commons.sample.jpa.repository.UserRepository;
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
