package com.haier.fintech.modules.sys.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.haier.fintech.common.exception.HaierException;
import com.haier.fintech.common.utils.Constant;
import com.haier.fintech.common.utils.PageUtils;
import com.haier.fintech.common.utils.Query;
import com.haier.fintech.modules.sys.dao.SysUserDao;
import com.haier.fintech.modules.sys.entity.SysUserEntity;
import com.haier.fintech.modules.sys.service.SysRoleService;
import com.haier.fintech.modules.sys.service.SysUserRoleService;
import com.haier.fintech.modules.sys.service.SysUserService;


/**
 * 系统用户
 * 
 * @author haier
 * @email haier@haier.com
 * @date 2016年9月18日 上午9:46:09
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysRoleService sysRoleService;
	@Value("${haier.isDev:false}")
	private boolean isDev;

	@Override
	public PageUtils queryPage(HashMap<String,Object> params) {
		SysUserEntity en = new SysUserEntity();
		Page<SysUserEntity> page = null;
		en = JSON.parseObject(JSON.toJSONString(params), SysUserEntity.class) ;
		EntityWrapper<SysUserEntity> eWrapper = new EntityWrapper<SysUserEntity>(en);
		
		page = this.selectPage(new Query<SysUserEntity>(params).getPage(),eWrapper); 

		return new PageUtils(page);
	}

	@Override
	public List<String> queryAllPerms(Long userId) {
		return baseMapper.queryAllPerms(userId);
	}

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return baseMapper.queryAllMenuId(userId);
	}

	@Override
	public SysUserEntity queryByUserName(String username) {
		return baseMapper.queryByUserName(username);
	}

	@Override
	@Transactional
	public void save(SysUserEntity user) {
		user.setCreateTime(new Date());
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
		user.setSalt(salt);
		this.insert(user);
		
		//检查角色是否越权
		checkRole(user);
		
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}
	
	@Override
	@Transactional
	public SysUserEntity saveLdap(String usernName) {
		List<Long> list = new ArrayList<Long>();
		list.add(1L);
		SysUserEntity user =new SysUserEntity();		
		user.setUsername(usernName);
		user.setStatus(1);
		user.setType(2);
		user.setRoleIdList(list);
		user.setCreateTime(new Date());
		this.insert(user);
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
		return user;
	}

	@Override
	@Transactional
	public void update(SysUserEntity user) {
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword(null);
		}else{
			user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
		}
		this.updateById(user);
		
		//检查角色是否越权
		checkRole(user);
		
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	public void deleteBatch(Long[] userId) {
		this.deleteBatchIds(Arrays.asList(userId));
	}

	@Override
	public boolean updatePassword(Long userId, String password, String newPassword) {
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setPassword(newPassword);
		return this.update(userEntity,
				new EntityWrapper<SysUserEntity>().eq("user_id", userId).eq("password", password));
	}
	
	/**
	 * 检查角色是否越权
	 */
	private void checkRole(SysUserEntity user){
		if(user.getRoleIdList() == null || user.getRoleIdList().size() == 0){
			return;
		}
		//如果不是超级管理员，则需要判断用户的角色是否自己创建
		//user.getCreateUserId() == Constant.SUPER_ADMIN
		if(isDev || user.getCreateUserId() == Constant.SUPER_ADMIN){
			return ;
		}
		
		//查询用户创建的角色列表
		List<Long> roleIdList = sysRoleService.queryRoleIdList(user.getCreateUserId());

		//判断是否越权
		if(!roleIdList.containsAll(user.getRoleIdList())){
			throw new HaierException("新增用户所选角色，不是本人创建");
		}
	}

	@Override
	public List<SysUserEntity> getUsersList(String word) {
		// TODO Auto-generated method stub
		return baseMapper.getUsersList(word);
	}

	@Override
	public List<SysUserEntity> getUsersListByDeptId(String deptId) {
		// TODO Auto-generated method stub
		return baseMapper.getUsersListByDeptId(deptId);
	}

	@Override
	public List<SysUserEntity> getUsersListByRoleId(String roleId) {
		// TODO Auto-generated method stub
		return baseMapper.getUsersListByRoleId(roleId);
	}
}
