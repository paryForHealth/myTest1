/*
 * Copyright (C), 北京中恒博瑞数字电力技术有限公司，保留所有权利.
 * FileName: LineSetupWithDeviceRelationService.java
 * History：
 * <author>         <time>             			 <version>      <desc>
 *   guowenchao   	 2016年7月8日下午3:50:44          V1.0         	 线路搭建和设备的关系维护服务
 */
package com.jb.equipment.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jb.dao.GenericDao;
import com.jb.data.DataTable;
import com.jb.util.platform.ResourceManager;

/**
 * @Package: com.jb.equipment.service
 * @ClassName: LineSetupWithDeviceRelationService
 * @Description:  线路搭建和设备的关系维护服务
 */
@Service("lineSetupWithDeviceRelationService")
public class LineSetupWithDeviceRelationService {
	
	@Resource(name = "genericDao")
	private GenericDao genericDao;
	
	/**
	 * 
	 * @Title: queryBylineDataGuid
	 * @Description: 根据搭建方式查询数据
	 * @param  setupId
	 * @return String
	 * @throws
	 */
	public String queryByLineDataGuid(String LineDataGuid) throws Exception{
		String sql = ResourceManager.getInstance().getDBSS("selectStepBySetupid");
		DataTable dataTable = genericDao.getDataIntoDataTable(sql, new Object[]{LineDataGuid});
		return dataTable.toJson();
	}
}
