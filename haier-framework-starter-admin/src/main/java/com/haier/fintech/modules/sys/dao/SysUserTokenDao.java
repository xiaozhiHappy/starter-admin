package com.haier.fintech.modules.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.haier.fintech.modules.sys.entity.SysUserTokenEntity;

import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户Token
 * 
 * @author haier
 * @email haier@haier.com
 * @date 2017-03-23 15:22:07
 */
@Mapper
public interface SysUserTokenDao extends BaseMapper<SysUserTokenEntity> {

    SysUserTokenEntity queryByToken(String token);
	
}
