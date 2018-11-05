package com.haier.fintech.modules.sys.service;



import java.util.List;
import java.util.Map;

import com.haier.fintech.modules.sys.entity.SysConfigEntity;

/**
 * 系统配置信息
 * 


 * @date 2016年12月4日 下午6:49:01
 */
public interface SysConfigService {
	
	/**
	 * 保存配置信息
	 */
	public void save(SysConfigEntity config);
	
	/**
	 * 更新配置信息
	 */
	public void update(SysConfigEntity config);
	
	/**
	 * 根据key，更新value
	 */
	public void updateValueByKey(String key, String value);
	
	/**
	 * 删除配置信息
	 */
	public void deleteBatch(Long[] ids);
	
	/**
	 * 获取List列表
	 */
	public List<SysConfigEntity> queryList(Map<String, Object> map);
	/**
	 * 获取总记录数
	 */
	public int queryTotal(Map<String, Object> map);
	
	public SysConfigEntity queryObject(Long id);
	
	/**
	 * 根据key，获取配置的value值
	 * 
	 * @param key           key
	 */
	public String getValue(String key);
	
	/**
	 * 根据key，获取value的Object对象
	 * @param key    key
	 * @param clazz  Object对象
	 */
	public <T> T getConfigObject(String key, Class<T> clazz);

	public List<SysConfigEntity> queryByKey(String key);

	public List<SysConfigEntity> queryParent(String id,String key);

	List<SysConfigEntity> queryListByParent(Map<String, Object> map);

	/**
	 * 落地接口字典验证方法
	 * @param parentValue 数据字典类型parentValue（zhuangXiu、chaoxiang等）
	 * @param value 当前落地数据value
	 */
	public boolean validateDictionary(String parentValue, String value);
	
}
