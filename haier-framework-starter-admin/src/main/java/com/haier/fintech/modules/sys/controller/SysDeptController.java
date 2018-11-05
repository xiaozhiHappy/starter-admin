package com.haier.fintech.modules.sys.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haier.fintech.common.utils.Constant;
import com.haier.fintech.common.utils.R;
import com.haier.fintech.modules.sys.entity.SysDeptEntity;
import com.haier.fintech.modules.sys.entity.SysUserEntity;
import com.haier.fintech.modules.sys.service.SysDeptService;


/**
 * 部门管理
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends AbstractController {
	@Autowired
	private SysDeptService sysDeptService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("sys:dept:list")
	public List<SysDeptEntity> list(){
		List<SysDeptEntity> deptList = sysDeptService.queryList(null);
		return deptList;
	}
	/**
	 * 选择部门(添加、修改菜单)
	 */
	@RequestMapping("/select")
	//@RequiresPermissions("sys:dept:select")
	public R select(){
		List<SysDeptEntity> deptList = sysDeptService.selectByMap(null);

		//添加一级部门
		if(getUserId() == Constant.SUPER_ADMIN){
			SysDeptEntity root = new SysDeptEntity();
			root.setDeptId("0");
			root.setName("一级部门");
			root.setParentId("");
			root.setOpen(true);
			deptList.add(root);
		}
		Collections.sort(deptList, new Comparator<SysDeptEntity>() {  
            public int compare(SysDeptEntity dept1, SysDeptEntity dept2) {  
                // 按照部门ID进行排序
                if (dept1.getDeptId().compareTo(dept2.getDeptId()) > 1) {  
                    return 1;  
                }  
                if (dept1.getDeptId().compareTo(dept2.getDeptId()) == 0) {  
                    return 0;  
                }  
                return -1;  
            }  
        });
		return R.ok().put("deptList", deptList);
	}

	/**
	 * 上级部门Id(管理员则为0)
	 */
	@RequestMapping("/info")
	//@RequiresPermissions("sys:dept:list")
	public R info(){
		String deptId = "";
		if(getUserId() != Constant.SUPER_ADMIN){
			SysDeptEntity dept = sysDeptService.selectById(super.getDeptId());
			deptId = dept.getParentId();
		}

		return R.ok().put("deptId", deptId);
	}
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{deptId}")
	//@RequiresPermissions("sys:dept:info")
	public R info(@PathVariable("deptId") String deptId){
		SysDeptEntity dept = sysDeptService.queryObject(deptId);
		
		return R.ok().put("dept", dept);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("sys:dept:save")
	public R save(@RequestBody SysDeptEntity dept){
		String deptId = UUID.randomUUID().toString().replace("-", "").toLowerCase();
		dept.setDeptId(deptId);
		sysDeptService.save(dept);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("sys:dept:update")
	public R update(@RequestBody SysDeptEntity dept){
		sysDeptService.updateById(dept);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("sys:dept:delete")
	public R delete(String deptId){
		//判断是否有子部门
		SysUserEntity sysUserEntity = getUser();
		if(sysUserEntity.getDeptId() != null){
			if(getDeptId().equals(deptId)) {
				return R.error("无法删除自己部门");
			}
		}
		List<String> deptList = sysDeptService.queryDetpIdList(deptId);
		if(!deptList.isEmpty()){
			return R.error("请先删除子部门");
		}
		
		sysDeptService.deleteById(deptId); 
		return R.ok();
	}
	
	@RequestMapping("/getDeptsList")
	public R getDeptsList(){
		List<SysDeptEntity> deptsList=sysDeptService.getDeptsList();
		return R.ok().put("deptsList", deptsList);
	}
	
}
