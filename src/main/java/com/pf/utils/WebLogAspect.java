package com.pf.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Aspect
@Component
public class WebLogAspect {

    private static final Logger log = LoggerFactory.getLogger(WebLogAspect.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("execution(public * com.pf.controller.*.*(..))")
    public void webLog() {
    }

    @Before("webLog()") // webLog():是你@Pointcut注解的方法名
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        // 打印请求相关参数
        // 打印请求 url
        log.info("URL            : {}", request.getRequestURL().toString());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 打印请求的 IP
        log.info("IP             : {}", request.getRemoteAddr());
        // 打印请求入参
        String argsString = objectMapper.writeValueAsString(joinPoint.getArgs());
        log.info("Request Args   : {}", argsString);

        // 获取 @ApiOperation("") 注解中的信息，此注解属于swagger的，但也能获取，其他注解可举一反三
        ApiOperation apiOperation = null;

        // JoinPoint：oinPoint对象封装了SpringAop中切面方法的信息,在切面方法中添加JoinPoint参数,就可以获取到封装了该方法信息的JoinPoint对象.大概就是获取目标方法的一些信息
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        apiOperation = ms.getMethod().getDeclaredAnnotation(ApiOperation.class);
        if (apiOperation != null) {
            log.info("Action         : {}", apiOperation.value());
        }
    }

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        // 打印出参
        String resultString = objectMapper.writeValueAsString(result);
        log.info("Response Args  : {}", resultString);
        // 执行耗时
        log.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
        log.info("=========================================== End ===========================================");
        System.out.println(" ");
        return result;
    }

    @After("webLog()")
    public void doAfter() {}

    @AfterThrowing(pointcut = "webLog()", throwing = "e")
    public void doAfterThrowing(Throwable e) {
        log.debug("Error Info  : {}", e.toString());
    }
}
