package org.example.test.service;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public interface AsyncDemoService {

    void asyncTaskWithoutResult(String message);

    Future<String> asyncTaskWithResult(String input);

    FutureTask<String> futureTaskWithResult(String s);

    Future<String> submitTaskWithResult(String s);
}
