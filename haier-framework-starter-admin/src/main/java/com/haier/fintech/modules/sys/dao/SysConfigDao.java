package com.haier.fintech.modules.sys.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.haier.fintech.modules.sys.entity.SysConfigEntity;

/**
 * 系统配置信息
 * 


 * @date 2016年12月4日 下午6:46:16
 */
@Mapper
public interface SysConfigDao {

	/**
	 * 根据key，查询value
	 */
	List<SysConfigEntity> queryListByKey(String paramKey);
	
	SysConfigEntity queryByKey(String paramKey);

	/**
	 * 根据key，更新value
	 */
	int updateValueByKey(@Param("key") String key, @Param("value") String value);

	List<SysConfigEntity> queryParentList(@Param("value") String id,@Param("key") String key);

	int validateDictionary(@Param("parentValue") String parentValue, @Param("value") String value);

	void update(SysConfigEntity config);

	void save(SysConfigEntity config);

	void deleteBatch(Long[] ids);

	List<SysConfigEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);

	SysConfigEntity queryObject(Long id);
	
}
