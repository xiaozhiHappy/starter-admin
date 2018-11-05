package com.haier.fintech.modules.sys.service.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.haier.fintech.common.utils.PageUtils;
import com.haier.fintech.common.utils.Query;
import com.haier.fintech.modules.sys.dao.SysDeptDao;
import com.haier.fintech.modules.sys.entity.SysConfigEntity;
import com.haier.fintech.modules.sys.entity.SysDeptEntity;
import com.haier.fintech.modules.sys.service.SysDeptService;



@Service("sysDeptService")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptDao, SysDeptEntity> implements SysDeptService {
	@Autowired 
	private SysDeptDao sysDeptDao;
	@Override
	public List<String> queryDetpIdList(String parentId) {
		// TODO Auto-generated method stub
		return baseMapper.queryDetpIdList(parentId);
	}

	@Override
	public String getSubDeptIdList(String deptId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SysDeptEntity> queryList(SysDeptEntity sysDeptEntity) {
		// TODO Auto-generated method stub
		return baseMapper.queryList(sysDeptEntity);
	}

	@Override
	public SysDeptEntity queryObject(String deptId) {
		// TODO Auto-generated method stub
		return baseMapper.queryObject(deptId);
	}

	@Override
	public PageUtils queryPage(HashMap<String, Object> params) {
		// TODO Auto-generated method stub
		String key = (String)params.get("key");

		Page<SysDeptEntity> page = this.selectPage(
				new Query<SysDeptEntity>(params).getPage(),
				new EntityWrapper<SysDeptEntity>()
					.like(StringUtils.isNotBlank(key),"parentId", key)
					.eq("delFlag", 0)
		);

		return new PageUtils(page);
	}

	@Override
	public List<SysDeptEntity> getDeptsList() {
		// TODO Auto-generated method stub
		List<SysDeptEntity> deptsList= baseMapper.getDeptsList();
		return deptsList;
	}

	@Override
	public void save(SysDeptEntity dept) {
		sysDeptDao.save(dept);
	}

}
