package org.example.test.controller;


import org.example.test.service.AsyncDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

@RestController
public class AsyncDemoController {

    @Autowired
    private AsyncDemoService asyncDemoService;

    /**
     * 测试无返回值异步任务
     * 访问: http://localhost:8080/fire-and-forget?message=Hello
     */
    @GetMapping("/fire-and-forget")
    public String fireAndForget(@RequestParam String message) {
        asyncDemoService.asyncTaskWithoutResult(message);
        return "任务已提交，请查看后台日志。主线程立即返回了！";
    }

    /**
     * 测试@Async有返回值任务
     * 访问: http://localhost:8080/async-result?input=testData
     */
    @GetMapping("/async-result")
    public String getAsyncResult(@RequestParam String input) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        Future<String> future = asyncDemoService.asyncTaskWithResult(input);

        // 主线程可以继续做其他工作...
        System.out.println("主线程在等待结果的同时可以处理其他事情...");

        // 当需要结果时，调用get()方法（会阻塞直到任务完成）
        String result = future.get();
        long duration = System.currentTimeMillis() - startTime;

        return "结果: " + result + " | 总耗时: " + duration + "ms (包含异步处理时间)";
    }

    /**
     * 对比测试：三种方式获取结果
     * 访问: http://localhost:8080/compare-methods?input=compare
     */
    @GetMapping("/compare-methods")
    public String compareMethods(@RequestParam String input) throws Exception {
        // 1. 使用@Async方式
        Future<String> future1 = asyncDemoService.asyncTaskWithResult(input + "_Async");
        String result1 = future1.get();

        // 2. 使用submit方式
        Future<String> future2 = asyncDemoService.submitTaskWithResult(input + "_Submit");
        String result2 = future2.get();

        // 3. 使用FutureTask方式
        FutureTask<String> future3 = asyncDemoService.futureTaskWithResult(input + "_FutureTask");
        String result3 = future3.get();

        return String.format("""
                对比结果:<br>
                1. @Async: %s<br>
                2. submit: %s<br>
                3. FutureTask: %s<br>
                <br>
                推荐在生产环境中使用第1种(@Async)方式。
                """, result1, result2, result3);
    }
}
