package com.haier.fintech.modules.sys.controller;

import java.util.HashMap;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.haier.fintech.common.annotation.SysLog;
import com.haier.fintech.common.utils.PageUtils;
import com.haier.fintech.common.utils.R;
import com.haier.fintech.modules.sys.entity.SysDraftEntity;
import com.haier.fintech.modules.sys.service.SysDraftService;

@RestController
@RequestMapping("/sys/draft")
public class SysDraftController extends AbstractController {
	@Autowired
	private SysDraftService sysDraftService;

	@GetMapping("/list")
	@RequiresPermissions("sys:draft:list")
	public R list(@RequestParam HashMap<String,Object> params){
		params.put("createUser", getUserId().toString());
		PageUtils page = sysDraftService.queryPage(params);
		return R.ok().put("page", page);
	}
	
	@GetMapping("/getInfo")
	@RequiresPermissions("sys:draft:info")
	public R info(@RequestParam HashMap<String,Object> params){
		params.put("createUser", getUserId().toString());
		SysDraftEntity draft = sysDraftService.select(params);
		return R.ok().put("draft", draft);
	}
	
	@GetMapping("/info/{id}")
	@RequiresPermissions("sys:draft:info")
	public R info(@PathVariable("id") Integer id){
		SysDraftEntity draft = sysDraftService.selectById(id);
		return R.ok().put("draft", draft);
	}
	
	@SysLog("保存草稿")
	@PostMapping("/save")
	@RequiresPermissions("sys:draft:save")
	public R save(@RequestBody JSONObject draftData ){
		SysDraftEntity draft = new SysDraftEntity();
		draft.setLinkId(draftData.getString("id"));
		draft.setDraftTitle(draftData.getString("name"));
		draft.setType(draftData.getInteger("type"));
		draft.setDraftData(draftData.getString("draftData"));
		draft.setCreateUser(getUserId().toString());
		draft.setUpdateUser(getUserId().toString());
		sysDraftService.save(draft);
		return R.ok();
	}
	
	@SysLog("修改草稿")
	@PostMapping("/update")
	@RequiresPermissions("sys:draft:update")
	public R update(@RequestBody JSONObject draftData){
		SysDraftEntity draft = new SysDraftEntity();
		draft.setId(draftData.getInteger("draftId"));
		draft.setLinkId(draftData.getString("id"));
		draft.setDraftTitle(draftData.getString("name"));
		draft.setType(draftData.getInteger("type"));
		draft.setDraftData(draftData.getString("draftData"));
		draft.setUpdateUser(getUserId().toString());
		sysDraftService.update(draft);
		return R.ok();
	}
	
	@SysLog("删除草稿")
	@PostMapping("/delete")
	@RequiresPermissions("sys:draft:delete")
	public R delete(@RequestBody List<Integer> ids){
		sysDraftService.deleteBatchIds(ids);
		return R.ok();
	}
}
