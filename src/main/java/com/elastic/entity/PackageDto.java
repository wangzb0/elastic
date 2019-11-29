package com.elastic.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PackageDto implements Serializable {
    private static final long serialVersionUID = 932894464776639209L;
    private Long id;
    private String code;
    private String userName;
    private Integer userType;
    private Long serverId;
    private String serverName;
    private Integer usedNum;
    private BigDecimal coinPrice;
    private String demoUrl;
    private String jsonDesc;
    private String statusStr;
    private Integer status;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Boolean canFreeToUse;
    private Boolean canGrant;
    private Long userId;
}
