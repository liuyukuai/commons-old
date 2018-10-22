package com.itxiaoer.commons.core;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author : liuyk
 */
public class Main {
    private List<Data> first = Arrays.asList(new Data("1", Collections.singletonList("1")), new Data("2", Collections.singletonList("2")));
    private List<Data> second = Collections.singletonList(new Data("2", Collections.singletonList("3")));

    @Test
    public void concat() {

        List<Data> result = Stream.concat(first.stream(), second.stream())
                .collect(Collectors.groupingBy(Data::getId))
                .entrySet()
                .stream()
                .map(data ->
                        data.getValue().stream().reduce((v1, v2) -> {
                                    v1.setIds(Stream.concat(v1.getIds().stream(), v2.getIds().stream()).collect(Collectors.toList()));
                                    return v1;
                                }
                        ).orElseThrow(RuntimeException::new)
                ).collect(Collectors.toList());

        Assert.assertEquals(Arrays.asList(new Data("1", Collections.singletonList("1")), new Data("2", Arrays.asList("2", "3"))), result);

    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Data {
        private String id;
        private List<String> ids;

    }
}
