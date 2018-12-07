package com.itxiaoer.commons.sample.service;

import com.itxiaoer.commons.jpa.service.BasicJpaService;
import com.itxiaoer.commons.sample.jpa.dto.UserDto;
import com.itxiaoer.commons.sample.jpa.entity.Browse;
import com.itxiaoer.commons.sample.jpa.entity.User;
import com.itxiaoer.commons.sample.jpa.repository.BrowseRepository;
import com.itxiaoer.commons.sample.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

@Service
@SuppressWarnings("WeakerAccess")
public class MockUserService extends BasicJpaService<UserDto, User, String, UserRepository> {
    private static volatile AtomicInteger i = new AtomicInteger(0);


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private BrowseRepository browseRepository;

    @Override
    public BiConsumer<User, UserDto> consumer() {
        return (dest, source) ->
                dest.setId(i.incrementAndGet() + "");

    }

    @Transactional(rollbackFor = Exception.class)
    public void browse(String id, String token) {
        this.browse(id, token, t -> {
            Optional<Browse> byId = browseRepository.findById(t);
            Browse browse = byId.orElse(new Browse(t, 0L));
            browse.setCount(browse.getCount() + 1);
            browseRepository.saveAndFlush(browse);
            return true;
        });
    }

    public Long getBrowse(String id) {
        Optional<Browse> byId = browseRepository.findById(id);
        return byId.map(Browse::getCount).orElse(0L);

    }


    @Transactional(rollbackFor = Exception.class)
    public void removeBrowse(String id, String token) {
        redisTemplate.delete(token + id);
        this.browseRepository.deleteById(id);
    }
}