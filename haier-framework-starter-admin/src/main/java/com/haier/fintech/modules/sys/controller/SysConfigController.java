package com.haier.fintech.modules.sys.controller;

import com.haier.fintech.common.annotation.SysLog;
import com.haier.fintech.common.utils.R;
import com.haier.fintech.common.validator.ValidatorUtils;
import com.haier.fintech.modules.sys.entity.SysConfigEntity;
import com.haier.fintech.modules.sys.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统配置信息
 */
@RestController
@RequestMapping("/sys/config")
public class SysConfigController extends AbstractController {
	@Autowired
	private SysConfigService sysConfigService;
	
	/**
	 * 所有配置列表
	 */
	@RequestMapping("/list")
	public List<SysConfigEntity> list(@RequestParam Map<String, Object> params){
		//查询列表数据
		List<SysConfigEntity> configList = sysConfigService.queryList(params);
		return configList;
	}

	@RequestMapping("/listByParent")
	public List<SysConfigEntity> listByParent(@RequestParam Map<String, Object> params){
		//查询列表数据
		return sysConfigService.queryListByParent(params);
	}
	
	@RequestMapping("/query")
	public R queryByKey(@RequestParam String key){
		//查询列表数据
		List<SysConfigEntity> configList = sysConfigService.queryByKey(key);
		return R.ok().put("configList", configList);
	}
	
	@RequestMapping("/queryParent")
	public R queryParent(String id,String key){
		//查询列表数据
		List<SysConfigEntity> configList = sysConfigService.queryParent(id,key);
		return R.ok().put("configList", configList);
	}
	
	@RequestMapping("/select")
    public R select(@RequestParam Map<String, Object> params) {
        List<SysConfigEntity> configList = sysConfigService.queryList(params);

		//添加一级部门
        //if(getUserId() == Constant.SUPER_ADMIN) {
			SysConfigEntity root = new SysConfigEntity();
			root.setId(0L);
        root.setKey("一级参数");
        root.setValue("0");
			root.setParentId(-1L);
			root.setOpen(true);
			configList.add(root);
        //}

		return R.ok().put("configList", configList);
	}
	
	/**
	 * 配置信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Long id){
		SysConfigEntity config = sysConfigService.queryObject(id);
		return R.ok().put("config", config);
	}
	
	/**
	 * 保存配置
	 */
    @SysLog("保存参数")
	@RequestMapping("/save")
	public R save(@RequestBody SysConfigEntity config){
		ValidatorUtils.validateEntity(config);

		sysConfigService.save(config);
		
		return R.ok();
	}
	
	/**
	 * 修改配置
	 */
    @SysLog("修改参数")
	@RequestMapping("/update")
	public R update(@RequestBody SysConfigEntity config){
		ValidatorUtils.validateEntity(config);
		
		sysConfigService.update(config);
		
		return R.ok();
	}
	
	/**
	 * 删除配置
	 */
    @SysLog("删除参数")
	@RequestMapping("/delete")
	public R delete(@RequestBody Long[] ids){
		sysConfigService.deleteBatch(ids);
		
		return R.ok();
	}



}
