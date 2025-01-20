package com.ss.minio.controller;

import com.ss.minio.dto.TaskInfoDTO;
import com.ss.minio.entity.UploadTaskEntity;
import com.ss.minio.req.InitTaskParam;
import com.ss.minio.res.Result;
import com.ss.minio.service.FileService;
import com.ss.minio.service.UploadTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Api(tags = "文件管理")
@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private UploadTaskService uploadTaskService;

    @ApiOperation("创建桶")
    @GetMapping("/create-bucket")
    public Result createBucket(@RequestParam("bucketName") String bucketName) {
        fileService.createBucket(bucketName);
        return Result.success();
    }

    @ApiOperation("获取所有桶")
    @GetMapping("/buckets")
    public Result getBuckets() {
        return Result.success(fileService.getAllBuckets());
    }

    @ApiOperation("单个文件上传")
    @PostMapping("/single-upload")
    public Result uploadFile(String bucketName, @RequestPart("file") MultipartFile file) {
        return Result.success(fileService.uploadFile(bucketName, file));
    }

    @ApiOperation("批量文件上传")
    @PostMapping("/batch-upload")
    @ApiImplicitParam(name = "files", value = "上传文件", dataTypeClass = MultipartFile.class, required = true)
    public Result batchUploadFile(String bucketName, @RequestPart("files") MultipartFile[] files) {
        return Result.success(fileService.uploadFile(bucketName, files));
    }

//    @ApiOperation("批量文件上传")
//    @PostMapping("/batch-upload")
//    @ApiImplicitParam(name = "files", value = "上传文件", dataTypeClass = MultipartFile.class, required = true)
//    public Result batchUploadFile(String bucketName, @RequestPart("files") MultipartFile[] files) {
//        return Result.success(fileService.uploadFile(bucketName, files));
//    }

    @ApiOperation("删除")
    @DeleteMapping("/")
    public Result uploadFile(String bucketName, String fileName) {
        fileService.deleteFile(bucketName, fileName);
        return Result.success();
    }

    @ApiOperation("获取文件完整地址")
    @GetMapping("/url")
    public Result getPresignedObjectUrl(String bucketName, String fileName, Integer expire) {
        return Result.success(fileService.getPresignedObjectUrl(bucketName, fileName, expire));
    }


    /**
     * 获取上传进度
     * @param identifier 文件md5
     * @return
     */
    @ApiOperation("获取上传进度")
    @GetMapping("/process/{identifier}")
    public Result taskInfo (@PathVariable("identifier") String identifier) {
        return Result.success(uploadTaskService.getTaskInfo(identifier));
    }

    /**
     * 创建一个上传任务
     * @return
     */
    @ApiOperation("创建一个上传任务")
    @PostMapping("/initTask")
    public Result initTask (@Valid @RequestBody InitTaskParam param, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(bindingResult.getFieldError().getDefaultMessage());
        }
        return Result.success(uploadTaskService.initTask(param));
    }

    /**
     * 获取每个分片的预签名上传地址
     * @param identifier
     * @param partNumber
     * @return
     */
    @ApiOperation("获取每个分片的预签名上传地址")
    @GetMapping("/sign/{identifier}/{partNumber}")
    public Result preSignUploadUrl (@PathVariable("identifier") String identifier, @PathVariable("partNumber") Integer partNumber) {
        UploadTaskEntity task = uploadTaskService.getByIdentifier(identifier);
        if (Objects.isNull(task)) {
            return Result.error("分片任务不存在");
        }
        Map<String, String> params = new HashMap<>();
        params.put("partNumber", partNumber.toString());
        params.put("uploadId", task.getUploadId());
        return Result.success().put("data",uploadTaskService.genPreSignUploadUrl(task.getBucketName(), task.getObjectKey(), params));
    }

    /**
     * 合并分片
     * @param identifier
     * @return
     */
    @ApiOperation("合并分片")
    @PostMapping("/merge/{identifier}")
    public Result merge (@PathVariable("identifier") String identifier) {
        uploadTaskService.merge(identifier);
        return Result.success();
    }

}
