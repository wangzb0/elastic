package com.elastic.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ESPackageQueryDTO {
    //分页数据
    private Page<PackageDto> page;
    //标签统计数据
    private List<ESPackageAggResultDTO> aggList;
}
