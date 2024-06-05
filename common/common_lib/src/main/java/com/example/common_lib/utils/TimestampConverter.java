package com.example.common_lib.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampConverter {
    /**
     * 将Unix时间戳（毫秒）转换为指定格式的日期时间字符串。
     *
     * @param timestamp 要转换的时间戳（毫秒）
     * @param formatPattern 输出日期时间的格式模式，如"yyyy-MM-dd HH:mm:ss"
     * @return 格式化后的日期时间字符串
     */
    public static String convertTimestampToString(long timestamp, String formatPattern) {
        if (timestamp <= 0) {
            throw new IllegalArgumentException("Timestamp must be positive.");
        }

        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);
        return sdf.format(date);
    }

    /**
     * 将Unix时间戳（毫秒）转换为指定格式的日期时间字符串。
     *  年月日

     */
    public static String convertTimestampToY_M_D_H_M_S(long timestamp) {
        if (timestamp <= 0) {
            throw new IllegalArgumentException("Timestamp must be positive.");
        }
        return convertTimestampToString(timestamp, "yyyy-MM-dd HH:mm:ss");
    }
}
