package com.nctigba.observability.instance.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nctigba.observability.instance.entity.NctigbaEnv;

@Mapper
public interface NctigbaEnvMapper extends BaseMapper<NctigbaEnv>{
}