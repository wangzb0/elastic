package com.elastic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 数据包表
 */
@Data
@TableName("t_package")
@Component
public class Package implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
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
    private LocalDateTime created;
    /**
     * 删除时间
     */
    private LocalDateTime deleted;
    /**
     * 更新时间
     */
    private LocalDateTime updated;
    /**
     * 文件路径（已标注数据导入数据库，Excel文件存放路径）
     */
    private String filePath;

}
