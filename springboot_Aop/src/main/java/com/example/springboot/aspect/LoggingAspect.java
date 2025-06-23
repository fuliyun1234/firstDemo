package com.example.springboot.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    // 定义切点，匹配UserService的所有方法
    @Pointcut("execution(* com.example.springboot.service.UserService.*(..))")
    public void userServiceMethods() {}

    // 前置通知
    @Before("userServiceMethods()")
    public void beforeAA() {
        System.out.println("Before ...");
    }

    // 后置通知
    @After("userServiceMethods()")
    public void afterAA() {
        System.out.println("After ..");
    }

    // 返回通知
    @AfterReturning("userServiceMethods()")
    public void afterReturningAA() {
        System.out.println("AfterReturning ...");
    }

    // 异常通知
    @AfterThrowing("userServiceMethods()")
    public void afterThrowingAA() {
        System.out.println("AfterThrowing ...");
    }

    // 环绕通知
    @Around("userServiceMethods()")
    public Object aroundAA(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("Before Around ...");
        Object result = proceedingJoinPoint.proceed();
        System.out.println("After Around ...");
        return result;
    }
}