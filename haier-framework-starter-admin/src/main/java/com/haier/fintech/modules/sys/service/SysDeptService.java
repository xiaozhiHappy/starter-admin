package com.haier.fintech.modules.sys.service;

import java.util.HashMap;
import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.haier.fintech.common.utils.PageUtils;
import com.haier.fintech.modules.sys.entity.SysDeptEntity;

/**
 * 部门管理
 * 


 * @date 2017-06-20 15:23:47
 */
public interface SysDeptService extends IService<SysDeptEntity>{

	/**
	 * 查询子部门ID列表
	 * @param parentId  上级部门ID
	 */
	List<String> queryDetpIdList(String parentId);

	/**
	 * 获取子部门ID(包含本部门ID)，用于数据过滤
	 */
	String getSubDeptIdList(String deptId);
	
	
	/**
	 * 部门列表
	 * @param sysDeptEntity
	 * @return
	 */
	List<SysDeptEntity> queryList(SysDeptEntity sysDeptEntity);

	SysDeptEntity queryObject(String deptId);

	PageUtils queryPage(HashMap<String, Object> params);

	List<SysDeptEntity> getDeptsList();

	void save(SysDeptEntity dept);

}
