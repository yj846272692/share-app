package com.soft1851.content.domain.dto;

import com.soft1851.content.domain.enums.AuditStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel("审核指定内容")
public class AuditDTO {


    @Column(name = "audit_status")
    @ApiModelProperty(name = "auditStatusEnum", value = "审核状态")
    private AuditStatusEnum auditStatusEnum;

    @Column(name = "reason")
    @ApiModelProperty(name = "reason", value = "原因")
    private String reason;


}
