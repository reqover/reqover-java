package io.reqover.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class AsyncTask {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void execute(Runnable task) {
        executorService.execute(task);
    }

    public void waitCompleted(){
        executorService.shutdown();
        while (!executorService.isTerminated()){}
    }

}

public class TestAsync {

    private static final AsyncTask task = new AsyncTask();

    @Test
    public void test1() {
        task.execute(() -> {
            System.out.println("Test 1 started");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Test 1 finished");
        });
    }

    @Test
    public void test2() {
        task.execute(() -> {
            System.out.println("Test 2 started");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Test 2 finished");
        });
    }

    @Test
    public void test3() {
        task.execute(() -> {
            System.out.println("Test 3 started");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Test 3 finished");
        });
    }

    @AfterAll
    public static void tearDown() {
        task.waitCompleted();
    }
}
