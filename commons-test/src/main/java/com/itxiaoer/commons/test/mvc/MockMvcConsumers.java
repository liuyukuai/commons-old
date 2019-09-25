package com.itxiaoer.commons.test.mvc;

import com.itxiaoer.commons.core.page.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.function.Consumer;

/**
 * @author : liuyk
 */
@Slf4j
@SuppressWarnings({"unused", "WeakerAccess"})
public final class MockMvcConsumers {

    private static final Consumer<ResultActions> DEFAULT_SUCCESS = (actions) -> {
        try {
            actions.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("true"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    };

    private static Consumer<ResultActions> DEFAULT_FAIL = (actions) -> {
        try {
            actions.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    };

    public static Consumer<ResultActions> ok() {
        return DEFAULT_SUCCESS;
    }

    public static <T> Consumer<ResultActions> ok(T t) {
        return DEFAULT_SUCCESS.andThen((actions) -> {
            try {
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.data").value(t));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });

    }

    public static Consumer<ResultActions> fail() {
        return DEFAULT_FAIL;
    }

    public static Consumer<ResultActions> fail(String code) {
        return (actions) -> {
            try {
                actions.andExpect(MockMvcResultMatchers.jsonPath("$.success").value("false"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(code));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        };
    }
}
