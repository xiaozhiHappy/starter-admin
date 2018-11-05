package com.haier.fintech.modules.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.haier.fintech.modules.sys.entity.SysDeptEntity;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 部门管理
 * 


 * @date 2017-06-20 15:23:47
 */
@Mapper
public interface SysDeptDao extends BaseMapper<SysDeptEntity>  {

    /**
     * 查询子部门ID列表
     * @param parentId  上级部门ID
     */
    List<String> queryDetpIdList(String parentId);

	SysDeptEntity getDeptByName(String name);
	void updateDeptByDeptId(SysDeptEntity dept);

	List<SysDeptEntity> getDepts();

	List<SysDeptEntity> queryList(SysDeptEntity sysDeptEntity);

	SysDeptEntity queryObject(String deptId);

	void updateOrg();

	List<SysDeptEntity> getDeptsList();

	void save(SysDeptEntity dept);
}
