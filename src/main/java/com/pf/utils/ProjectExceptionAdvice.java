package com.pf.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ProjectExceptionAdvice {
    @ExceptionHandler
    public R doException(Exception e) {
        log.error("Exception      : {}", e.getLocalizedMessage());
        log.info("=========================================== End ===========================================");
        System.out.println(" ");
        if (e.getLocalizedMessage().contains("Required request body is missing")) {
            return R.fail("Bad Request! Required request body is missing");
        }
        if (e.getLocalizedMessage().contains("is not supported")) {
            return R.fail("Bad Request! Method is not supported");
        }
        if (e.getLocalizedMessage().contains("Duplicate entry")) {
            String[] messages = e.getLocalizedMessage().split("'");
            return R.fail("SQL Error! Duplicate entry: " + messages[1]);
        }
        return new R(500, e.getLocalizedMessage());
    }
}
