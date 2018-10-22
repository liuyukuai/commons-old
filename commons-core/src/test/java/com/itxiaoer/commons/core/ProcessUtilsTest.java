package com.itxiaoer.commons.core;

import com.itxiaoer.commons.core.beans.ProcessUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

/**
 * @author : liuyk
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ProcessUtilsTest {

    private Source source;

    @Data
    @NoArgsConstructor
    static class Source {
        private String name;
        private boolean b1;
        private Boolean b2;
        private LocalDate time;
        private int a;
        private long b;
        private Bar bar;
    }

    @Data
    @NoArgsConstructor
    static class Dest {
        private String name;
        private boolean b1;
        private boolean b2;
        private LocalDate time;
        private int a;
        private long b;
        private Bar bar;
    }


    @Data
    static class Bar {
        private String name;
        private Byte b;
    }


    @Before
    public void init() {
        source = new Source();
        source.name = "source";
        source.b1 = true;
        source.b2 = false;
        source.time = LocalDate.now();
        source.a = 1;
        source.b = 2L;
        Bar bar = new Bar();
        bar.name = "bar";
        bar.b = 5;
        source.bar = bar;
    }

    @Test
    public void process() {
        Dest dest = new Dest();
        ProcessUtils.processObject(dest, this.source);
        System.out.println(source);
        System.out.println(dest);
    }
}
