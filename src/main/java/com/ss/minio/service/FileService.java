package com.ss.minio.service;

import com.ss.minio.config.MinioConfig;
import com.ss.minio.enums.ExceptionEnums;
import com.ss.minio.exception.BaseException;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 文件操作
 */
@Slf4j
@Service
public class FileService {

    @Autowired
    private MinioConfig minioConfig;

    /**
     * 验证是否可以连接
     */
    public void verifiedLink() {
        try {
            getAllBuckets();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException(HttpStatus.FORBIDDEN.value(), "连接失败，请检查配置");
        }
    }

    /**
     * 初始化Bucket
     */
    private void createBucket(String bucketName) {
        // 判断 BucketName 是否存在
        if (!bucketExists(bucketName)) {
            return;
        }
        try {
            minioConfig.minioClient().makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException(ExceptionEnums.BUCKET_NAME_NOT_NULL.getMsg());
        }
    }

    /**
     * 验证bucketName是否存在
     *
     * @return boolean true:存在
     */
    private boolean bucketExists(String bucketName) {
        if (!StringUtils.hasText(bucketName)) {
            throw new BaseException(ExceptionEnums.BUCKET_NAME_NOT_NULL.getMsg());
        }
        boolean flag;
        try {
            flag = minioConfig.minioClient().bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException(ExceptionEnums.BUCKET_NAME_NOT_NULL.getMsg());
        }
        return flag;
    }


    /**
     * 获取全部 bucket
     */
    public List<String> getAllBuckets() {
        List<String> list;
        try {
            final List<Bucket> buckets = minioConfig.minioClient().listBuckets();
            list = new ArrayList<>(buckets.size());
            for (Bucket bucket : buckets) {
                list.add(bucket.name());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException(ExceptionEnums.BUCKET_ACQUIRE_FAIL.getMsg());
        }
        return list;
    }


    /**
     * 删除文件
     *
     * @param bucketName bucket名称
     * @param fileName   文件名称
     *                   说明：当前方法不能真正删除，需要验证
     */
    public void deleteFile(String bucketName, String fileName) {
        if (StringUtils.hasText(bucketName)) {
            bucketExists(bucketName);
        }
        bucketName = !StringUtils.hasText(bucketName) ? minioConfig.getMinioProperties().getBucketName() : bucketName;
        if (!StringUtils.hasText(fileName)) {
            throw new BaseException(ExceptionEnums.FILE_NAME_NOT_NULL.getMsg());
        }
        try {
            minioConfig.minioClient().removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException("文件删除失败");
        }
    }

    /**
     * 批量文件删除
     *
     * @param bucketName bucket名称
     * @param fileNames  文件名
     */
    public void deleteBatchFile(String bucketName, List<String> fileNames) {
        if (StringUtils.hasText(bucketName)) {
            bucketExists(bucketName);
        }
        bucketName = !StringUtils.hasText(bucketName) ? minioConfig.getMinioProperties().getBucketName() : bucketName;
        if (CollectionUtils.isEmpty(fileNames)) {
            throw new BaseException(ExceptionEnums.FILE_NAME_NOT_NULL.getMsg());
        }
        try {
            List<DeleteObject> objects = new LinkedList<>();
            for (String fileName : fileNames) {
                objects.add(new DeleteObject(fileName));
            }
            Iterable<Result<DeleteError>> results =
                    minioConfig.minioClient().removeObjects(
                            RemoveObjectsArgs.builder().bucket(bucketName).objects(objects).build());
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                log.error("Error occurred: " + error);
                throw new BaseException("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除失败！error:{}", e.getMessage());
            throw new BaseException("批量删除失败");
        }
    }

    /**
     * 文件文件大小转换
     *
     * @param fileS
     * @return
     */
    private static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0 B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + " B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + " KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + " MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + " GB";
        }
        return fileSizeString;
    }
}
