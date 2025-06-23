package com.fly.test.context;


/**
 * 使用 ThreadLocal 存储当前线程的数据源标识
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDataSource(String db) {
        contextHolder.set(db);
    }

    public static String getDataSource() {
        return contextHolder.get();
    }

    public static void clearDataSource() {
        contextHolder.remove();
    }
}