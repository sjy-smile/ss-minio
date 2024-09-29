package com.ss.minio.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel("minio分页查询—入参")
@Data
@EqualsAndHashCode(callSuper = true)
public class MinioConfigPageReq extends PageParam {

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "账号")
    private String accessKey;
}
