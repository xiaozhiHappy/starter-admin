package com.haier.fintech.modules.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.haier.fintech.modules.sys.entity.SysRoleEntity;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色管理
 * 
 * @author haier
 * @email haier@haier.com
 * @date 2016年9月18日 上午9:33:33
 */
@Mapper
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {
	
	/**
	 * 查询用户创建的角色ID列表
	 */
	List<Long> queryRoleIdList(Long createUserId);

	List<SysRoleEntity> getRolesList();

}
