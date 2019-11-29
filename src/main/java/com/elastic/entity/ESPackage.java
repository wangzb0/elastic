package com.elastic.entity;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ESPackage {
    private Long id;
    private String code;
    /**
     * 所属用户ID
     */
    private Long userId;
    /**
     * 所属节点ID，0为云节点
     */
    private Long serverId;
    /**
     * 样列下载地址
     */
    private String demoUrl;
    /**
     * 包json描述
     */
    private String jsonDesc;
    /**
     * 数据包被购买次数
     */
    private Integer usedNum;
    private BigDecimal coinPrice;
    /**
     * 状态,1-待上架，2-已上架，3-已下架 4-暂不分享
     */
    private Integer status;
    /**
     * 排序
     */
    private Integer sortNo;
    /**
     * 创建时间
     */
    private String created;
    /**
     * 删除时间
     */
    //private LocalDateTime deleted;
    /**
     * 更新时间
     */
    //private LocalDateTime updated;
    /**
     * 文件路径（已标注数据导入数据库，Excel文件存放路径）
     */
    private String filePath;

    // 将 jsonDesc 字符串转换为 JSONObject 存入ES
    private JSONObject tagJson;

    public static ESPackage getInstance(Package pkg) {
        ESPackage esPackage = new ESPackage();
        BeanUtils.copyProperties(pkg, esPackage);
        if (!StringUtils.isEmpty(pkg.getJsonDesc())
                && JSONUtil.isJsonObj(pkg.getJsonDesc())) {
            esPackage.setTagJson(JSONUtil.parseObj(pkg.getJsonDesc()));
        }
        if (!ObjectUtils.isEmpty(pkg.getCreated())) {
            esPackage.setCreated(pkg.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        return esPackage;
    }
}
