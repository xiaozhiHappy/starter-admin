package com.haier.fintech.modules.sys.service;

import java.util.HashMap;

import com.baomidou.mybatisplus.service.IService;
import com.haier.fintech.common.utils.PageUtils;
import com.haier.fintech.modules.sys.entity.SysDraftEntity;

public interface SysDraftService extends IService<SysDraftEntity> {

	void save(SysDraftEntity draft);

	void update(SysDraftEntity draft);

	PageUtils queryPage(HashMap<String, Object> params);

	SysDraftEntity select(HashMap<String, Object> params);
}
