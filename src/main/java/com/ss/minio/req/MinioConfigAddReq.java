package com.ss.minio.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("minio配置信息添加—入参")
@Data
public class MinioConfigAddReq {

    @ApiModelProperty(value = "名称", required = true)
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "对象存储URL", required = true)
    @NotBlank(message = "对象存储URL不能为空")
    private String endpoint;

    @ApiModelProperty(value = "账号", required = true)
    @NotBlank(message = "账号不能为空")
    private String accessKey;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String secretKey;

    @ApiModelProperty(value = "默认桶名", required = true)
    @NotBlank(message = "桶名不能为空")
    private String defaultBucket;

    @ApiModelProperty(value = "是否选择 1 选择 2 未选择", required = true,allowableValues = "1,2")
    @NotNull(message = "是否选择不能为空")
    @Min(value = 1,message = "可选择值 1-2")
    @Max(value = 2,message = "可选择值 1-2")
    private Integer isChoose;

    @ApiModelProperty(value = "过期时间", required = true)
    @NotNull(message = "过期时间不能为空，单位秒")
    private Integer expire;
}
