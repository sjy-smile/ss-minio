package com.ss.minio.controller;


import com.ss.minio.entity.MinioConfigEntity;
import com.ss.minio.req.MinioConfigAddReq;
import com.ss.minio.req.MinioConfigEditReq;
import com.ss.minio.req.MinioConfigPageReq;
import com.ss.minio.res.Result;
import com.ss.minio.service.FileService;
import com.ss.minio.service.MinioConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * minio配置信息 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2024-09-25
 */
@Api(tags = "Minio配置管理")
@RestController
@RequestMapping("/minio")
public class MinioConfigController {

    @Autowired
    private MinioConfigService minioConfigService;

    @Autowired
    private FileService fileService;

    @ApiOperation("配置信息列表")
    @GetMapping("/list")
    public Result getList() {
        return Result.success(minioConfigService.list());
    }

    @ApiOperation("分页条件查询")
    @GetMapping("/page")
    public Result selectMinioConfigPage(@Validated MinioConfigPageReq req) {
        return Result.success(minioConfigService.selectMinioConfigPage(req));
    }

    @ApiOperation("新增配置信息")
    @PostMapping
    public Result addMinioConfig(@Validated @RequestBody MinioConfigAddReq req) {
        minioConfigService.addMinioConfig(req);
        return Result.success();
    }

    @ApiOperation("修改配置信息")
    @PostMapping("/edit")
    public Result editMinioConfig(@Validated @RequestBody MinioConfigEditReq req) {
        minioConfigService.editMinioConfig(req);
        return Result.success();
    }

    @ApiOperation("删除配置信息")
    @DeleteMapping("/{id}")
    public Result deleteMinioConfig(@PathVariable("id") Long id) {
        minioConfigService.deleteMinioConfig(id);
        return Result.success();
    }

    @ApiOperation("切换minio配置")
    @GetMapping("/switch/{id}")
    public Result switchMinioConfig(@PathVariable("id") Long id) {
        minioConfigService.switchMinioConfig(id);
        return Result.success();
    }

    @ApiOperation("刷新配置")
    @GetMapping("/refresh")
    public Result refreshMinioConfig(Long id) {
        minioConfigService.refreshMinioConfig(id);
        return Result.success();
    }

    @ApiOperation("验证minio连接")
    @GetMapping("/verified")
    public Result verifiedMinioLink() {
        fileService.verifiedLink();
        return Result.success();
    }
}

