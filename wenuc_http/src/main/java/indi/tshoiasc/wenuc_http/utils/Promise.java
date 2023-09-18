package indi.tshoiasc.wenuc_http.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Promise {

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    private Promise() {
        throw new AssertionError();
    }

    public static <T> List<T> all(final List<Callable<T>> callableList) throws InterruptedException {
        final List<T> result = new ArrayList<>();
        int length = callableList.size();
        final CountDownLatch ready = new CountDownLatch(length);
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch done = new CountDownLatch(length);
        for (final Callable<T> callable : callableList) {
            executorService.execute(() -> {
                ready.countDown();
                try {
                    start.await();
                    T t = callable.call();
                    result.add(t);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                    result.add(null);
                    e.printStackTrace();
                } finally {
                    done.countDown();
                }
            });
        }
        ready.await();
        long startnano = System.nanoTime();
        start.countDown();
        done.await();
        long cause = System.nanoTime() - startnano;
//        System.out.println(String.format("Promise all done,cause time millSecond: %s", cause / 1000000));
        return result;
    }

}