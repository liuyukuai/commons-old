package com.itxiaoer.commons.test.mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author : liuyk
 */
@Slf4j
@SuppressWarnings({"unused", "WeakerAccess"})
public final class MockMvcExecutor {

    private MockMvc mockMvc;


    private MockMvcExecutor(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public static MockMvcExecutor of(MockMvc mockMvc) {
        return new MockMvcExecutor(mockMvc);
    }

    public void ok(Supplier<MockHttpServletRequestBuilder> supplier) throws Exception {
        this.execute(supplier, MockMvcConsumers.ok());
    }

    public void ok(Supplier<MockHttpServletRequestBuilder> supplier, Consumer<String> returned) throws Exception {
        this.execute(supplier, MockMvcConsumers.ok(), returned);
    }

    public void fail(Supplier<MockHttpServletRequestBuilder> supplier) throws Exception {
        this.execute(supplier, MockMvcConsumers.fail());
    }

    public void fail(Supplier<MockHttpServletRequestBuilder> supplier, Consumer<ResultActions> consumer) throws Exception {
        this.execute(supplier, consumer);
    }

    public void execute(Supplier<MockHttpServletRequestBuilder> supplier, Consumer<ResultActions> consumer) throws Exception {
        this.execute(supplier, consumer, null);
    }

    public void execute
            (Supplier<MockHttpServletRequestBuilder> supplier, Consumer<ResultActions> consumer, Consumer<String> returned) throws Exception {
        MockHttpServletRequestBuilder mockBuilder = supplier.get().contentType(MediaType.APPLICATION_JSON_UTF8);
        ResultActions perform = mockMvc.perform(mockBuilder);
        perform = perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));
        if (consumer != null) {
            consumer.accept(perform);
        }

        if (returned != null) {
            returned.accept(perform.andReturn().getResponse().getContentAsString());
        }
    }

}
