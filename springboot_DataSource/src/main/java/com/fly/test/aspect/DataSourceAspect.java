package com.fly.test.aspect;


import com.fly.test.annotation.DataSource;
import com.fly.test.context.DataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 通过 AOP 动态切换数据源。
 *
 */
@Aspect
@Component
public class DataSourceAspect {

    /**
     * 定义切点：拦截 com.example.service 包下的所有方法
     */
    @Pointcut("execution(* com.fly.test.service.*.*(..))")
    public void serviceMethods() {
    }


    @Before("serviceMethods()")
    public void beforeSwitchDataSource(JoinPoint point) {
        // 获取类名
        String className = point.getTarget().getClass().getSimpleName();

        // 根据类名切换数据源
        if (className.contains("Primary")) {
            DataSourceContextHolder.setDataSource("primaryDataSource");
        } else if (className.contains("Secondary")) {
            DataSourceContextHolder.setDataSource("secondaryDataSource");
        } else {
            DataSourceContextHolder.setDataSource("primaryDataSource"); // 默认数据源
        }
    }

    @After("execution(* com.fly.test.service.*.*(..))")
    public void afterSwitchDataSource(JoinPoint point) {
        DataSourceContextHolder.clearDataSource(); // 清除数据源
    }
}