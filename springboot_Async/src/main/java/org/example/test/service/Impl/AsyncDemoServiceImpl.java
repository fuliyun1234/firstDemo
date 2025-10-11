package org.example.test.service.Impl;

import org.example.test.service.AsyncDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;


@Service
public class AsyncDemoServiceImpl implements AsyncDemoService {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor; // 注入配置的线程池

    //==================== 1. 使用 @Async 注解 (推荐度: ⭐⭐⭐⭐⭐) ====================

    /**
     * 无返回值任务 - @Async方式 (Fire and Forget)
     * 模拟记录日志、发送通知等不需要结果的操作
     */
    @Override
    @Async("taskExecutor") // 指定使用我们配置的线程池
    public void asyncTaskWithoutResult(String message) {
        try {
            System.out.println(Thread.currentThread().getName() + " 开始执行无返回任务: " + message);
            Thread.sleep(3000); // 模拟耗时操作
            System.out.println(Thread.currentThread().getName() + " 无返回任务完成: " + message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 有返回值任务 - @Async方式
     * 模拟数据处理、计算等需要获取结果的操作
     */
    @Override
    @Async("taskExecutor")
    public Future<String> asyncTaskWithResult(String input) {
        return new AsyncResult<>(doHeavyLifting(input)); // 使用AsyncResult包装结果
    }

    //==================== 2. 使用 submit(Callable) (推荐度: ⭐⭐⭐☆☆) ====================

    /**
     * 有返回值任务 - submit(Callable)方式
     */
    @Override
    public Future<String> submitTaskWithResult(String input) {
        // 直接提交Callable，返回Future
        return taskExecutor.submit(() -> doHeavyLifting(input));
    }

    //==================== 3. 使用 execute(FutureTask) (推荐度: ⭐☆☆☆☆) ====================

    /**
     * 有返回值任务 - execute(FutureTask)方式
     * 代码繁琐，不推荐，仅作演示
     */
    @Override
    public FutureTask<String> futureTaskWithResult(String input) {
        // 手动创建Callable和FutureTask
        Callable<String> task = () -> doHeavyLifting(input);
        FutureTask<String> futureTask = new FutureTask<>(task);
        // 使用execute提交，而不是submit，避免创建双重Future
        taskExecutor.execute(futureTask);
        return futureTask;
    }

    //==================== 模拟的耗时业务方法 ====================
    private String doHeavyLifting(String input) {
        try {
            System.out.println(Thread.currentThread().getName() + " 正在处理: " + input);
            Thread.sleep(2000); // 模拟2秒耗时操作
            String result = "处理结果: " + input.toUpperCase();
            System.out.println(Thread.currentThread().getName() + " 处理完成: " + result);
            return result;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "处理被中断";
        }
    }
}
