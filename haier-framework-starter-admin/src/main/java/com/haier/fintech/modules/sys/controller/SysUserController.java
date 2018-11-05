package com.haier.fintech.modules.sys.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haier.fintech.common.annotation.SysLog;
import com.haier.fintech.common.utils.Constant;
import com.haier.fintech.common.utils.PageUtils;
import com.haier.fintech.common.utils.R;
import com.haier.fintech.common.validator.Assert;
import com.haier.fintech.common.validator.ValidatorUtils;
import com.haier.fintech.common.validator.group.AddGroup;
import com.haier.fintech.common.validator.group.UpdateGroup;
import com.haier.fintech.modules.sys.entity.SysDeptEntity;
import com.haier.fintech.modules.sys.entity.SysRoleEntity;
import com.haier.fintech.modules.sys.entity.SysUserEntity;
import com.haier.fintech.modules.sys.form.PasswordForm;
import com.haier.fintech.modules.sys.service.SysDeptService;
import com.haier.fintech.modules.sys.service.SysRoleService;
import com.haier.fintech.modules.sys.service.SysUserRoleService;
import com.haier.fintech.modules.sys.service.SysUserService;

/**
 * 系统用户
 * 
 * @author haier
 * @email haier@haier.com
 * @date 2016年10月31日 上午10:40:10
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysRoleService sysRoleService;

	/**
	 * 所有用户列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:user:list")
	public R list(@RequestParam HashMap<String,Object> params){
		//只有超级管理员，才能查看所有管理员列表
		/*if(getUserId() != Constant.SUPER_ADMIN){
			params.put("createUserId", getUserId());
		}*/
		PageUtils page = sysUserService.queryPage(params);

		return R.ok().put("page", page);
	}
	
	/**
	 * 获取登录的用户信息
	 */
	@GetMapping("/info")
	public R info(){
		SysUserEntity user = getUser();
 		user.setSalt("");
 		user.setPassword("");
 		//获取用户所属的角色列表
 		if(user.getUserId()== Constant.SUPER_ADMIN){
 			List<SysRoleEntity> list = sysRoleService.selectByMap(null);
 			List<Long> roleIdList = new ArrayList<Long>();
 			for (SysRoleEntity role : list) {
 				roleIdList.add(role.getRoleId());
			}
 			user.setRoleIdList(roleIdList);
 			return R.ok().put("user", user);
 		}
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(user.getUserId());
		user.setRoleIdList(roleIdList);
 		return R.ok().put("user", user);
	}
	
	/**
	 * 修改登录用户密码
	 */
	@SysLog("修改密码")
	@PostMapping("/password")
	public R password(@RequestBody PasswordForm form){
		Assert.isBlank(form.getNewPassword(), "新密码不为能空");
		
		//sha256加密
		String password = new Sha256Hash(form.getPassword(), getUser().getSalt()).toHex();
		//sha256加密
		String newPassword = new Sha256Hash(form.getNewPassword(), getUser().getSalt()).toHex();
				
		//更新密码
		boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
		if(!flag){
			return R.error("原密码不正确");
		}
		
		return R.ok();
	}
	
	/**
	 * 用户信息
	 */
	@GetMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public R info(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.selectById(userId);
		
		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);
		
		//获取用户所属的部门信息
		SysDeptEntity sysDept = sysDeptService.selectById(user.getDeptId());
		user.setDeptName(sysDept.getName());
		
		return R.ok().put("user", user);
	}
	
	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@PostMapping("/save")
	@RequiresPermissions("sys:user:save")
	public R save(@RequestBody JSONObject userJSON ){
		SysUserEntity user = JSON.toJavaObject(userJSON, SysUserEntity.class);
		ValidatorUtils.validateEntity(user, AddGroup.class); 
		
		user.setCreateUserId(getUserId());
		sysUserService.save(user);
		
		return R.ok();
	}
	
	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@PostMapping("/update")
	@RequiresPermissions("sys:user:update")
	public R update(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);

		user.setCreateUserId(getUserId());
		sysUserService.update(user);
		
		return R.ok();
	}
	
	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@PostMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return R.error("系统管理员不能删除");
		}
		
		if(ArrayUtils.contains(userIds, getUserId())){
			return R.error("当前用户不能删除");
		}
		
		sysUserService.deleteBatch(userIds);
		
		return R.ok();
	}
	
	@RequestMapping("/getUsersList")
	public R getUsersList(String word){
		List<SysUserEntity> usersList=sysUserService.getUsersList(word);
		return R.ok().put("usersList", usersList);
	}
	
	@RequestMapping("/getUsersListByDeptId")
	public R getUsersListByDeptId(String deptId){
		List<SysUserEntity> usersList=sysUserService.getUsersListByDeptId(deptId);
		return R.ok().put("usersList", usersList);
	}
	
	@RequestMapping("/getUsersListByRoleId")
	public R getUsersListByRoleId(String roleId){
		List<SysUserEntity> usersList=sysUserService.getUsersListByRoleId(roleId);
		return R.ok().put("usersList", usersList);
	}
}
