package com.pf.utils;

public class BaseContext {

    // 用于存取requestUser
    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void setRequestUser(String requestUser) {
        threadLocal.set(requestUser);
    }

    public static String getRequestUser() {
        return threadLocal.get();
    }
}
