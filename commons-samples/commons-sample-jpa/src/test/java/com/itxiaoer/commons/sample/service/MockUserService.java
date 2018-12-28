package com.itxiaoer.commons.sample.service;

import com.itxiaoer.commons.jpa.service.BasicJpaService;
import com.itxiaoer.commons.sample.jpa.dto.UserDto;
import com.itxiaoer.commons.sample.jpa.entity.User;
import com.itxiaoer.commons.sample.jpa.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

@Service
@SuppressWarnings("WeakerAccess")
public class MockUserService extends BasicJpaService<UserDto, User, String, UserRepository> {
    private static volatile AtomicInteger i = new AtomicInteger(0);



    @Override
    public BiConsumer<User, UserDto> consumer() {
        return (dest, source) ->
                dest.setId(i.incrementAndGet() + "");

    }

}
