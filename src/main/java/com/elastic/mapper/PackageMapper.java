package com.elastic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.elastic.entity.Package;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PackageMapper extends BaseMapper<Package> {

    List<Package> getByNeedSyncData(@Param("cursorTimestamp") Long cursorTimestamp);
}
