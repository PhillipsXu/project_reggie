package com.pf.utils;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class GenerateUtil {

    private static final String[] CHARACTER = new String[]{
                    "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                    "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                    "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

    public static String generateUsername() {
        StringBuilder username = new StringBuilder("reggie_");
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            username.append(CHARACTER[random.nextInt(CHARACTER.length + 1)]);
        }
        return username.toString();
    }

    public static String generateOrderNumber() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate now = LocalDate.now();
        String prefix = dateTimeFormatter.format(now);

        return prefix + IdWorker.getId();
    }
}
