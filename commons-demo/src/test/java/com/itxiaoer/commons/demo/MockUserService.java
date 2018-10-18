package com.itxiaoer.commons.demo;

import com.itxiaoer.commons.demo.dto.UserDto;
import com.itxiaoer.commons.demo.entity.User;
import com.itxiaoer.commons.demo.repository.UserRepository;
import com.itxiaoer.commons.jpa.service.BaseJpaService;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

@Service
public class MockUserService extends BaseJpaService<UserDto, User, String, UserRepository> {
    private static volatile AtomicInteger i = new AtomicInteger(0);


    @Override
    public BiConsumer<User, UserDto> consumer() {
        return (dest, source) ->
                dest.setId(i.incrementAndGet() + "");

    }
}
