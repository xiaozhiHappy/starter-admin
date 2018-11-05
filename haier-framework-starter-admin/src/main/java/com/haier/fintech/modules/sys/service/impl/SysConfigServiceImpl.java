package com.haier.fintech.modules.sys.service.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.haier.fintech.common.exception.HaierException;
import com.haier.fintech.modules.sys.dao.SysConfigDao;
import com.haier.fintech.modules.sys.entity.SysConfigEntity;
import com.haier.fintech.modules.sys.redis.SysConfigRedis;
import com.haier.fintech.modules.sys.service.SysConfigService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.event.NamingListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service("sysConfigService")
public class SysConfigServiceImpl implements SysConfigService {
	@Autowired
	private SysConfigDao sysConfigDao;
	@Autowired
	private SysConfigRedis sysConfigRedis;
	
	@Override
	@Transactional
	public void save(SysConfigEntity config) {
		sysConfigDao.save(config);
		sysConfigRedis.saveOrUpdate(config);
	}

	@Override
	@Transactional
	public void update(SysConfigEntity config) {
		sysConfigDao.update(config);
		sysConfigRedis.saveOrUpdate(config);
	}

	@Override
	@Transactional
	public void updateValueByKey(String key, String value) {
		sysConfigDao.updateValueByKey(key, value);
		sysConfigRedis.delete(key);
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] ids) {
		for(Long id : ids){
			SysConfigEntity config = queryObject(id);
			sysConfigRedis.delete(config.getKey());
		}

		sysConfigDao.deleteBatch(ids);
	}

	@Override
	public List<SysConfigEntity> queryList(Map<String, Object> map) {
        List<SysConfigEntity> list = sysConfigDao.queryList(map);
        List<Long> tids = new ArrayList<Long>();
        if (null != map.get("tid")) {
            tids.add(Long.valueOf(String.valueOf(map.get("tid"))));
        }
        removeListValue(list, tids, new ArrayList<Long>());
        return list;
    }

    @Override
    public List<SysConfigEntity> queryListByParent(Map<String, Object> map) {
        List<SysConfigEntity> list = sysConfigDao.queryList(map);
        final Long parentId = 2L;
        List<SysConfigEntity> oneList = new ArrayList<SysConfigEntity>();
        for (SysConfigEntity sysConfigEntity : list) {
            if (sysConfigEntity.getParentId().equals(parentId)) {
                oneList.add(sysConfigEntity);
            }
        }


        List<SysConfigEntity> result = new ArrayList<SysConfigEntity>();

        for (SysConfigEntity sysConfigEntity : oneList) {
            findSonTree(sysConfigEntity.getId(), list, result);
        }

        result.addAll(oneList);

        return result;
    }

    private void findSonTree(Long id, List<SysConfigEntity> list, List<SysConfigEntity> result) {
        List<SysConfigEntity> childList = new ArrayList<>();

        for (SysConfigEntity sysConfigEntity : list) {
            if (sysConfigEntity.getParentId().equals(id)) {
                childList.add(sysConfigEntity);
                result.add(sysConfigEntity);
            }
        }

        for (SysConfigEntity sysConfigEntity : childList) {
            for (SysConfigEntity configEntity : list) {
                if (sysConfigEntity.getId().equals(configEntity.getParentId())) {
                    findSonTree(configEntity.getId(), list, result);
                }
            }
        }
    }




    /**
     * 移除子元素
     */
    private void removeListValue(List<SysConfigEntity> list, List<Long> tids, List<Long> alls) {
        List<Long> longs = new ArrayList<Long>();
        for (SysConfigEntity sysConfigEntity : list) {
            for (Long l : tids) {
                if (sysConfigEntity.getParentId().equals(l)) {
                    longs.add(sysConfigEntity.getId());
                    alls.add(sysConfigEntity.getId());
                }
            }
        }

        if (!(CollectionUtils.isEmpty(longs))) {
            removeListValue(list, longs, alls);
        }

        Iterator<SysConfigEntity> it = list.iterator();
        while (it.hasNext()) {
            SysConfigEntity x = it.next();
            for (Long l : alls) {
                if (x.getId().equals(l)) {
                    it.remove();
                }
            }
        }
    }

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysConfigDao.queryTotal(map);
	}

	@Override
	public SysConfigEntity queryObject(Long id) {
		return sysConfigDao.queryObject(id);
	}

	@Override
	public String getValue(String key) {
		SysConfigEntity config = sysConfigRedis.get(key);
		if(config == null){
			config = sysConfigDao.queryByKey(key);
			sysConfigRedis.saveOrUpdate(config);
		}

		return config == null ? null : config.getValue();
	}
	
	@Override
	public <T> T getConfigObject(String key, Class<T> clazz) {
		String value = getValue(key);
		if(StringUtils.isNotBlank(value)){
			return new Gson().fromJson(value, clazz);
		}

		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new HaierException("获取参数失败");
		}
	}

	@Override
	public List<SysConfigEntity> queryByKey(String key) {
		// TODO Auto-generated method stub
		return sysConfigDao.queryListByKey(key);
	}

	@Override
	public List<SysConfigEntity> queryParent(String id,String key) {
		// TODO Auto-generated method stub
		return sysConfigDao.queryParentList(id,key);
	}
	
	/**
	 * 落地接口字典验证方法
	 * @param parentValue 数据字典类型parentValue（zhuangXiu、chaoxiang等）
	 * @param value 当前落地数据value
	 */
	@Override
	public boolean validateDictionary(String parentValue, String value) {
		int count = sysConfigDao.validateDictionary(parentValue, value);
		return count > 0 ? true : false;
	}
	
}
