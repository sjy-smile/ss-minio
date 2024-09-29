package com.ss.minio.config;

import com.ss.minio.constant.Constant;
import com.ss.minio.enums.ExceptionEnums;
import com.ss.minio.exception.BaseException;
import com.ss.minio.service.MinioConfigService;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * Minio 配置文件
 */
@Configuration
//@ConditionalOnClass(MinioProperties.class)
@EnableConfigurationProperties(value = MinioProperties.class)
public class MinioConfig {

    @Autowired
    private MinioProperties minioProperties;

    @Autowired
    private MinioConfigService minioConfigService;

    private MinioClient minioClient;

    @PostConstruct
    public void loadMinioProperties() {
        MinioProperties initialProperties = minioConfigService.getMinioConfigByIsChoose(Constant.IS_CHOOSE);
        if (Objects.nonNull(initialProperties)) {
            this.minioProperties = initialProperties;
            this.minioClient = createMinioClient(initialProperties);
        }
    }

    /**
     * 创建minio客户端
     * @param properties
     * @return
     */
    private MinioClient createMinioClient(MinioProperties properties) {
        return MinioClient.builder()
                .endpoint(properties.getEndpoint())
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .build();
    }


    public MinioProperties getMinioProperties() {
        return minioProperties;
    }


//    /**
//     * Minio 客户端连接配置
//     *
//     * @return
//     */
//    @Bean
//    public MinioClient minioClient() {
//        return MinioClient.builder()
//                .endpoint(minioProperties.getEndpoint())
//                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
//                .build();
//    }

    /**
     * 获取minio客户端
     */
    public MinioClient minioClient() {
        return minioClient;
    }

    // 手动刷新配置
    public void refreshMinioConfig(Long id) {
        MinioProperties newMinioProperties = Objects.nonNull(id) ? minioConfigService.getMinioConfigById(id) : minioConfigService.getMinioConfigByIsChoose(Constant.IS_CHOOSE);
        if (Objects.isNull(newMinioProperties)) {
            throw new BaseException(ExceptionEnums.MINIO_CONFIG_NULL.getMsg());
        }
        this.minioProperties = newMinioProperties;
        this.minioClient = createMinioClient(newMinioProperties);
    }

}
