package com.elastic.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ESPackageAggDetailDTO {
    //子表达式
    private String subexpression;
    //显示值&查询值
    private String value;
    //数量
    private Long docCount;
    //显示值说明
    private String valueDesc;
}