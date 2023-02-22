package com.springboot.blog.utils;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class MethodDetailsAdvisor {

    private static final Logger logger = LogManager.getLogger(MethodDetailsAdvisor.class);

    @Autowired
    private Util util;

    @Around("@annotation(MethodDetails) && execution(* * (..))")
    public Object methodDetails(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = null;
        String message = getMessage(joinPoint);
        logger.info(message + " -- START");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            logMethodArgument(joinPoint.getArgs(), message);
            result = joinPoint.proceed();
        } catch (Exception e) {
            logger.error("Exception in LoggerAroundAdvisor.methodDetails()", e);
            throw e;
        } finally {
            logResult(result, message);
            stopWatch.stop();
            logger.info(message + " -- END");
            logger.info("Elapsed Time (in Milliseconds): " + stopWatch.getTotalTimeMillis());
        }

        return result;
    }

    public void logMethodArgument(Object[] objects, String message) throws Throwable {

        if (objects != null) {
            Object[] var1 = objects;
            for (int var2 = 0; var2 < objects.length; ++var2) {
                util.printLog(var1[var2], message);
            }
        }
    }

    public void logResult(Object object, String message) {

        util.printLog(object, message);
    }

    public String getMessage(JoinPoint joinPoint) throws Exception {

        StringBuilder sb = new StringBuilder();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        sb.append(method.getDeclaringClass()).append(".").append(method.getName()).append(" ");
        return sb.toString();
    }
}
