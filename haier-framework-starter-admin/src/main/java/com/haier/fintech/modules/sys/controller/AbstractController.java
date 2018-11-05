package com.haier.fintech.modules.sys.controller;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haier.fintech.modules.sys.entity.SysUserEntity;

/**
 * Controller公共组件
 * 
 * @author haier
 * @email haier@haier.com
 * @date 2016年11月9日 下午9:42:26
 */
public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected SysUserEntity getUser() {
		return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
	}

	protected Long getUserId() {
		return getUser().getUserId();
	}
	
	protected String getDeptId() {
		return getUser().getDeptId();
	}
}
