package com.ss.minio.service;

import com.ss.minio.config.MinioConfig;
import com.ss.minio.enums.ExceptionEnums;
import com.ss.minio.exception.BaseException;
import com.ss.minio.utils.CommonUtils;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;

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
    public void createBucket(String bucketName) {
        // 判断 BucketName 是否存在
        if (bucketExists(bucketName)) {
            throw new BaseException("桶【" + bucketName + "】已存在");
        }
        try {
            minioConfig.minioClient().makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
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
     * 判断文件是否存在
     *
     * @param fileName 对象
     * @return true：存在
     */
    public boolean doFileNameExist(String bucketName, String fileName) {
        if (!StringUtils.hasText(bucketName)) {
            throw new BaseException(ExceptionEnums.BUCKET_NAME_NOT_NULL.getMsg());
        }
        if (!StringUtils.hasText(fileName)) {
            throw new BaseException(ExceptionEnums.FILE_NAME_NOT_NULL.getMsg());
        }
        boolean exist = true;
        try {
            minioConfig.minioClient().statObject(StatObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (Exception e) {
            exist = false;
        }
        return exist;
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
            e.printStackTrace();
            throw new BaseException(ExceptionEnums.BUCKET_ACQUIRE_FAIL.getMsg());
        }
        return list;
    }

    /**
     * 单个文件上传
     */
    public Map<String, Object> uploadFile(String bucketName, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BaseException(ExceptionEnums.FILE_NAME_NOT_NULL.getMsg());
        }
        bucketName = !StringUtils.hasText(bucketName) ? minioConfig.getMinioProperties().getBucketName() : bucketName;
        String originalName = file.getOriginalFilename();
        assert originalName != null;
        String newFileName = CommonUtils.getFileName(originalName);
        try {
            //文件上传
            InputStream in = file.getInputStream();
            minioConfig.minioClient().putObject(PutObjectArgs.builder().bucket(bucketName).object(newFileName).stream(in, file.getSize(), -1).contentType(file.getContentType()).build());
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new BaseException("文件上传失败");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("bucketName", bucketName);
        data.put("fileName", originalName);
        data.put("url", getPresignedObjectUrl(bucketName, newFileName, minioConfig.getMinioProperties().getExpire()));
        return data;
    }

    /**
     * 文件批量上传
     *
     * @param file
     * @return
     */
    public Map<String, Object> uploadFile(String bucketName, MultipartFile[] file) {
        if (file == null || file.length == 0) {
            throw new BaseException(ExceptionEnums.FILE_NAME_NOT_NULL.getMsg());
        }
        bucketName = !StringUtils.hasText(bucketName) ? minioConfig.getMinioProperties().getBucketName() : bucketName;
        List<String> originalNameList = new ArrayList<>(file.length);
        for (MultipartFile multipartFile : file) {
            String originalName = multipartFile.getOriginalFilename();
            // 文件名
            assert originalName != null;
            String newFileName = CommonUtils.getFileName(originalName);
            try {
                //文件上传
                InputStream in = multipartFile.getInputStream();
                minioConfig.minioClient().putObject(PutObjectArgs.builder().bucket(bucketName).object(newFileName).stream(in, multipartFile.getSize(), -1).contentType(multipartFile.getContentType()).build());
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
            // 完整路径
            originalNameList.add(getPresignedObjectUrl(bucketName, originalName, null));
        }
        Map<String, Object> data = new HashMap<>();
        data.put("bucketName", bucketName);
        data.put("fileName", originalNameList);
        return data;
    }

    /**
     * 删除文件
     *
     * @param bucketName bucket名称
     * @param fileName   文件名 完整路径（不包含bucket）
     *                   说明：当前方法不能真正删除，需要验证
     */
    public void deleteFile(String bucketName, String fileName) {
        if (StringUtils.hasText(bucketName) && !bucketExists(bucketName)) {
            throw new BaseException(ExceptionEnums.BUCKET_NOT_EXIST.getMsg());
        }
        bucketName = !StringUtils.hasText(bucketName) ? minioConfig.getMinioProperties().getBucketName() : bucketName;
        if (!StringUtils.hasText(fileName)) {
            throw new BaseException(ExceptionEnums.FILE_NAME_NOT_NULL.getMsg());
        }
        try {
            minioConfig.minioClient().removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (Exception e) {
            e.printStackTrace();
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
        if (StringUtils.hasText(bucketName) && !bucketExists(bucketName)) {
            throw new BaseException(ExceptionEnums.BUCKET_NOT_EXIST.getMsg());
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
     * 获取上传文件的完整路径
     *
     * @param bucketName 桶名称
     * @param fileName   文件名
     * @param expire     地址过期时间
     * @return
     */
    public String getPresignedObjectUrl(String bucketName, String fileName, Integer expire) {
        if (!StringUtils.hasText(fileName)) {
            throw new BaseException(ExceptionEnums.FILE_NAME_NOT_NULL.getMsg());
        }
        expire = Objects.isNull(expire) ? minioConfig.getMinioProperties().getExpire() : expire;
        // 验证桶是否存在在
        if (!bucketExists(bucketName)) {
            throw new BaseException(ExceptionEnums.BUCKET_NOT_EXIST.getMsg());
        }
        // 验证文件是否存在
        boolean validationFileName = doFileNameExist(bucketName, fileName);
        if (!validationFileName) {
            throw new BaseException(ExceptionEnums.FILE_NOT_EXIST.getMsg());
        }
        String url = null;
        try {
            // 获取桶和文件的完整路径
            fileName = fileName.startsWith("/") ? fileName.substring(fileName.indexOf("/") + 1) : fileName;
            url = minioConfig.minioClient().getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(fileName).method(Method.GET).expiry(expire).build());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error occurred: " + e);
        }
        return url;
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
