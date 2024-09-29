package com.ss.minio.controller;

import com.ss.minio.res.Result;
import com.ss.minio.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "文件管理")
@RestController
@RequestMapping("/file")
public class FileController {


}
