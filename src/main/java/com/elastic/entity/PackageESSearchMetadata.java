package com.elastic.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageESSearchMetadata {
    /**
     * 桶名
     */
    private String bucketName;
    /**
     * 别名
     */
    private String alias;
    /**
     * 说明
     */
    private String description;
    /**
     * 搜索关键字
     */
    private String searchKeyed;
    /**
     * 搜索关键字数组
     */
    private String[] searchKeyedArray;
    /**
     * 搜索关键字说明
     */
    private Map<String, String> valueDescMap;
    /**
     * 数据类型: 基因/影像
     */
    private String projectDataType;
}
