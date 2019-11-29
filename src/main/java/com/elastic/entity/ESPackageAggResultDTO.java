package com.elastic.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ESPackageAggResultDTO {
    //桶名
    private String bucketName;
    //别名
    private String alias;
    //说明
    private String desc;
    //数据类型: 基因/影像
    private String projectDataType;
    //统计数据详情
    private List<ESPackageAggDetailDTO> data;
}
