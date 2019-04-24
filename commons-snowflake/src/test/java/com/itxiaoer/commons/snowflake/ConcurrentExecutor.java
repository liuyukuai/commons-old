package com.itxiaoer.commons.snowflake;

import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author : liuyk
 */
@SuppressWarnings("WeakerAccess")
public class ConcurrentExecutor {


    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    public static Map<Long, Long> map = new ConcurrentHashMap<>();
    private static IdWorker idWorker = new IdWorker(0, 0);

    public static void execute(int con) {
        // 准备线程
        for (int i = 0; i < con; i++) {
            new Thread(new Task(countDownLatch, idWorker)).start();
        }
        // 使用计数器-1,100线程同时执行
        countDownLatch.countDown();
    }


    @AllArgsConstructor
    static class Task implements Runnable {

        private CountDownLatch countDownLatch;

        private IdWorker idWorker;


        @Override
        public void run() {
            try {
                countDownLatch.await();
                long id = idWorker.nextId();
                Long aLong = map.putIfAbsent(id, id);
                System.out.println(id);
                if (aLong != null) {
                    throw new IllegalArgumentException(id + "");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
