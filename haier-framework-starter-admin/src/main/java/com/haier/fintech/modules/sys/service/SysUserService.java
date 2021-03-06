package com.haier.fintech.modules.sys.service;

import java.util.HashMap;
import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.haier.fintech.common.utils.PageUtils;
import com.haier.fintech.modules.sys.entity.SysUserEntity;


/**
 * 系统用户
 * 
 * @author haier
 * @email haier@haier.com
 * @date 2016年9月18日 上午9:43:39
 */
public interface SysUserService extends IService<SysUserEntity> {

	PageUtils queryPage(HashMap<String,Object> params);

	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);

	/**
	 * 根据用户名，查询系统用户
	 */
	SysUserEntity queryByUserName(String username);

	/**
	 * 保存用户
	 */
	void save(SysUserEntity user);
	
	/**
	 * 修改用户
	 */
	void update(SysUserEntity user);
	
	/**
	 * 删除用户
	 */
	void deleteBatch(Long[] userIds);

	/**
	 * 修改密码
	 * @param userId       用户ID
	 * @param password     原密码
	 * @param newPassword  新密码
	 */
	boolean updatePassword(Long userId, String password, String newPassword);

	SysUserEntity saveLdap(String usernName);

	List<SysUserEntity> getUsersList(String word);

	List<SysUserEntity> getUsersListByDeptId(String deptId);

	List<SysUserEntity> getUsersListByRoleId(String roleId);
}
