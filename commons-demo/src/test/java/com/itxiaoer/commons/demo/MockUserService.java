package com.itxiaoer.commons.demo;

import com.itxiaoer.commons.demo.dto.UserDto;
import com.itxiaoer.commons.demo.entity.Browse;
import com.itxiaoer.commons.demo.entity.User;
import com.itxiaoer.commons.demo.repository.BrowseRepository;
import com.itxiaoer.commons.demo.repository.UserRepository;
import com.itxiaoer.commons.jpa.service.BaseJpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

@Service
public class MockUserService extends BaseJpaService<UserDto, User, String, UserRepository> {
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


    public void removeBrowse(String id, String token) {
        redisTemplate.delete(token + id);
    }
}
