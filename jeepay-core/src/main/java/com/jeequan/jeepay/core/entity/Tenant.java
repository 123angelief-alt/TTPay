package com.jeequan.jeepay.core.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jeequan.jeepay.core.model.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Schema(description = "租户表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_tenant")
public class Tenant extends BaseModel implements Serializable {

    public static final LambdaQueryWrapper<PayInterfaceConfig> gw(){
        return new LambdaQueryWrapper<>();
    }

    private static final long serialVersionUID=1L;

    @TableId
    @Schema(title = "tenantId", description = "租户id")
    private Long tenantId;

    @Schema(title = "tenantName", description = "租户名称")
    private String tenantName;

    @Schema(title = "tenantCode", description = "租户代码")
    private String tenantCode;

    @Schema(title = "domainUrl", description = "租户二级域名（根据域名判断是哪个租户来的请求）")
    private String domainUrl;


    @Schema(title = "remark", description = "备注")
    private String remark;

    @Schema(title = "state", description = "状态 0-停用, 1-启用")
    private Integer state;
    @Schema(title = "createdAt", description = "创建时间")
    private Date createdAt;

    /**
     * 更新时间
     */
    @Schema(title = "updatedAt", description = "更新时间")
    private Date updatedAt;
}
