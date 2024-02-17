//package com.intuit.commentsService.config.aspect;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class ExecutionLogger {
//    @Around("@annotation(com.intuit.commentsService)")
//    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
//        final long start = System.currentTimeMillis();
//
//        final Object proceed = joinPoint.proceed();
//
//        final long executionTime = System.currentTimeMillis() - start;
//
//        System.out.println(joinPoint.getSignature() + " executed in " + executionTime + "ms");
//
//        return proceed;
//    }
//}
