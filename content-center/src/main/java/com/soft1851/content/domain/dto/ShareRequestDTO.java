package com.soft1851.content.domain.dto;


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
@ApiModel("编辑投稿")
public class ShareRequestDTO {
    @ApiModelProperty(name = "userId",value = "投稿人id")
    private Integer userId;


    @Column(name = "author")
    @ApiModelProperty(name = "author", value = "作者")
    private String author;

    @Column(name = "download_url")
    @ApiModelProperty(name = "downloadUrl", value = "下载地址")
    private String downloadUrl;

    @Column(name = "is_original")
    @ApiModelProperty(name = "isOriginal", value = "是否原创 0：不是 1：是")
    private Boolean isOriginal;

    @Column(name = "price")
    @ApiModelProperty(name = "price", value = "价格")
    private Integer price;

    @Column(name = "summary")
    @ApiModelProperty(name = "summary", value = "简介")
    private String summary;

    @Column(name = "title")
    @ApiModelProperty(name = "title", value = "标题")
    private String title;

}
