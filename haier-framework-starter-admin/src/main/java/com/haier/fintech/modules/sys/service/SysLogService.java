package com.haier.fintech.modules.sys.service;


import com.baomidou.mybatisplus.service.IService;
import com.haier.fintech.common.utils.PageUtils;
import com.haier.fintech.modules.sys.entity.SysLogEntity;

import java.util.Map;


/**
 * 系统日志
 * 
 * @author haier
 * @email haier@haier.com
 * @date 2017-03-08 10:40:56
 */
public interface SysLogService extends IService<SysLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

}
