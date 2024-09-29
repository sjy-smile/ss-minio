package com.ss.minio.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ss.minio.config.MinioConfig;
import com.ss.minio.config.MinioProperties;
import com.ss.minio.constant.Constant;
import com.ss.minio.entity.MinioConfigEntity;
import com.ss.minio.exception.BaseException;
import com.ss.minio.mapper.MinioConfigMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ss.minio.req.MinioConfigAddReq;
import com.ss.minio.req.MinioConfigEditReq;
import com.ss.minio.req.MinioConfigPageReq;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * <p>
 * minio配置信息 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2024-09-25
 */
@Service
public class MinioConfigService extends ServiceImpl<MinioConfigMapper, MinioConfigEntity> {

    @Autowired
    private MinioConfigMapper minioConfigMapper;

    @Autowired
    @Lazy  // 解决循环依赖注入
    private MinioConfig minioConfig;

    /**
     * 根据id查询配置信息
     *
     * @param id
     * @return
     */
    public MinioProperties getMinioConfigById(Long id) {
        MinioConfigEntity minioConfig = minioConfigMapper.selectById(id);
        if (Objects.isNull(minioConfig)) {
            return null;
        }
        MinioProperties properties = new MinioProperties();
        BeanUtils.copyProperties(minioConfig, properties);
        properties.setBucketName(minioConfig.getDefaultBucket());
        return properties;
    }

    /**
     * 查询已选择minio配置信息
     *
     * @param isChoose
     * @return
     */
    public MinioProperties getMinioConfigByIsChoose(Integer isChoose) {
        MinioConfigEntity minioConfig = getMinioConfigIsChoose(isChoose);
        if (Objects.isNull(minioConfig)) {
            return null;
        }
        MinioProperties properties = new MinioProperties();
        BeanUtils.copyProperties(minioConfig, properties);
        properties.setBucketName(minioConfig.getDefaultBucket());
        return properties;
    }

    private MinioConfigEntity getMinioConfigIsChoose(Integer isChoose) {
        LambdaQueryWrapper<MinioConfigEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MinioConfigEntity::getIsChoose, isChoose);
        return minioConfigMapper.selectOne(queryWrapper);
    }

    /**
     * 分页条件查询
     *
     * @param req
     * @return
     */
    public Page<MinioConfigEntity> selectMinioConfigPage(MinioConfigPageReq req) {
        LambdaQueryWrapper<MinioConfigEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(req.getName())) {
            queryWrapper.eq(MinioConfigEntity::getName, req.getName());
        }
        if (StringUtils.hasText(req.getAccessKey())) {
            queryWrapper.eq(MinioConfigEntity::getAccessKey, req.getAccessKey());
        }
        return minioConfigMapper.selectPage(new Page<>(req.getPageNo(), req.getPageSize()), queryWrapper);
    }

    /**
     * 添加配置信息
     *
     * @param req
     */
    public void addMinioConfig(MinioConfigAddReq req) {
        MinioConfigEntity entity = new MinioConfigEntity();
        BeanUtils.copyProperties(req, entity);
        minioConfigMapper.insert(entity);
        // 切换minio配置信息
        switchMinioConfig(entity.getId());
    }

    /**
     * 修改配置信息
     *
     * @param req
     */
    @Transactional(rollbackFor = Exception.class)
    public void editMinioConfig(MinioConfigEditReq req) {
        MinioConfigEntity entity = new MinioConfigEntity();
        BeanUtils.copyProperties(req, entity);
        minioConfigMapper.updateById(entity);
        // 切换minio配置信息
        switchMinioConfig(entity.getId());
    }


    /**
     * 手动刷新配置
     *
     * @param id id可选
     */
    public void refreshMinioConfig(Long id) {
        minioConfig.refreshMinioConfig(id);
    }

    /**
     * 切换minio配置
     *
     * @param id
     */
    public void switchMinioConfig(Long id) {
        Integer count = minioConfigMapper.selectCount(new LambdaQueryWrapper<MinioConfigEntity>().eq(MinioConfigEntity::getIsChoose, Constant.IS_CHOOSE));
        if (count > 0) {
            this.update(new LambdaUpdateWrapper<MinioConfigEntity>().set(MinioConfigEntity::getIsChoose, Constant.NO_CHOOSE));
        }
        this.update(new LambdaUpdateWrapper<MinioConfigEntity>().set(MinioConfigEntity::getIsChoose, Constant.IS_CHOOSE).eq(MinioConfigEntity::getId, id));
        minioConfig.refreshMinioConfig(id);
    }

    /**
     * 删除配置信息
     *
     * @param id
     */
    public void deleteMinioConfig(Long id) {
        MinioConfigEntity configEntity = minioConfigMapper.selectById(id);
        if (Objects.isNull(configEntity)) {
            throw new BaseException("数据不存在");
        }
        minioConfigMapper.deleteById(id);
    }
}
