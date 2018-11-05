package com.haier.fintech.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@TableName("sys_draft")
public class SysDraftEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@TableId
	private Integer id;
	
	private String linkId;
	
	private String draftTitle;
	
	private String draftData;
	
	private Integer type;
	
    private String createUser;
	
	private String updateUser;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@TableField(value="create_time",fill = FieldFill.INSERT)
	private Date createTime;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@TableField(value="update_time",fill=FieldFill.INSERT_UPDATE)
	private Date updateTime;
	
}
