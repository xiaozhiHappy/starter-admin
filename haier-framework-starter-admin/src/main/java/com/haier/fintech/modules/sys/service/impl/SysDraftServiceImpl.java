package com.haier.fintech.modules.sys.service.impl;

import java.util.Date;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.haier.fintech.common.utils.PageUtils;
import com.haier.fintech.common.utils.Query;
import com.haier.fintech.modules.sys.dao.SysDraftDao;
import com.haier.fintech.modules.sys.entity.SysDraftEntity;
import com.haier.fintech.modules.sys.service.SysDraftService;


@Service("sysDraftService")
public class SysDraftServiceImpl extends ServiceImpl<SysDraftDao, SysDraftEntity> implements SysDraftService {

	@Override
	public void save(SysDraftEntity draft) {
		// TODO Auto-generated method stub
		draft.setCreateTime(new Date());
		draft.setUpdateTime(new Date());
		this.insert(draft);
	}

	@Override
	public void update(SysDraftEntity draft) {
		// TODO Auto-generated method stub
		draft.setUpdateTime(new Date());
		this.updateById(draft);
	}

	@Override
	public PageUtils queryPage(HashMap<String, Object> params) {
		// TODO Auto-generated method stub
		SysDraftEntity draft = JSON.parseObject(JSON.toJSONString(params), SysDraftEntity.class) ;
		EntityWrapper<SysDraftEntity> eWrapper = new EntityWrapper<SysDraftEntity>();
		eWrapper.eq("create_user", draft.getCreateUser());
		if(draft.getType()!=null){
			eWrapper.eq("type", draft.getType());
		}
		eWrapper.like("draft_title", draft.getDraftTitle());
		eWrapper.orderBy("update_time", false);
		Page<SysDraftEntity>  page = this.selectPage(new Query<SysDraftEntity>(params).getPage(),eWrapper); 
		return new PageUtils(page);
	}

	@Override
	public SysDraftEntity select(HashMap<String, Object> params) {
		// TODO Auto-generated method stub
		SysDraftEntity draft= new SysDraftEntity();
		draft=JSON.parseObject(JSON.toJSONString(params), SysDraftEntity.class) ;
		EntityWrapper<SysDraftEntity> eWrapper = new EntityWrapper<SysDraftEntity>(draft);
		SysDraftEntity sysDraft=this.selectOne(eWrapper);
		return sysDraft;
	}

}
