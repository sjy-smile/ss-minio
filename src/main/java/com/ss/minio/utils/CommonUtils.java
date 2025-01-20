package com.ss.minio.utils;

import java.util.Date;
import java.util.UUID;

/**
 * 通用方法
 */
public class CommonUtils {

    /**
     * 获取文件名及上传路径
     *
     * @param fileName
     * @return
     */
    public static String getFileName(String fileName) {
        return "/".concat(DateUtils.getDayDateString(new Date())).concat("/").concat(UUID.randomUUID().toString()).concat(fileName.substring(fileName.lastIndexOf(".")));
    }
}
