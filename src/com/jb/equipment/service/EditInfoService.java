/*
 * Copyright (C), 北京中恒博瑞数字电力技术有限公司，保留所有权利.
 * FileName: EditInfoService.java
 * History：
 * <author>         <time>             <version>      <desc>
 *   ghz     2016年7月6日下午2:09:36        V1.0         TODO
 */
package com.jb.equipment.service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.jb.core.constant.ICnsView;
import com.jb.dao.GenericDao;
import com.jb.data.DataTable;
import com.jb.data.InvokeResult;
import com.jb.equipment.constants.Constant.StationEquipmentType;
import com.jb.f1.kernel.util.ThreadLocalUtils;
import com.jb.model.UserModel;
import com.jb.model.engine.SingleBDService;

/**
 * 编辑信息获取
 * 
 * @Package: com.jb.equipment.service<br>
 * @ClassName: EditInfoService<br>
 * @Description: 编辑信息获取<br>
 */
@Service("editInfoService")
public class EditInfoService {

	/** 数据仓库 */
	@Resource(name = "genericDao")
	protected GenericDao genericDao;

	@Resource(name = "singleBDService")
	private SingleBDService sigleBDService;
	
	/**
	 * @Title: getEquEditInfo
	  */
	public DataTable getEquEditInfo() {
		String sql = " select * from us_app.tb_dev_sb_editinfo ";
		return this.genericDao.getDataIntoDataTable(sql, new Object[] {});
	}
	
	/**
	 * 
	 * @Title: deleteEquipment
	 * @Description: 删除设备
	 * @param 
	 * @return InvokeResult
	 * @throws
	 */
	public InvokeResult deleteEquipment(StationEquipmentType type,String appId,String classId,String objId){
		
		String stationCanDelSql = " select count(1) as num from us_app.tb_sb_bdsb_jgdy where  SSZF =? ";
		
		String jgdyCanDelSql = " select count(1) as num from us_app.tv_jgdy_sb where JGDY =? ";
		
		InvokeResult result = new InvokeResult();
		result.setSuccessful(false);
		
		HttpSession session = (HttpSession) ThreadLocalUtils
				.getObjectFromThreadLocal("session");
		UserModel user = (UserModel) session
				.getAttribute(ICnsView.LOGIN_USERMODEL);

		switch(type){
		case jgdy: 
			int equipmentCount = Integer.parseInt(String
					.valueOf(genericDao
							.getDataIntoDataTable(jgdyCanDelSql,
									new Object[] { objId }).getRows().get(0)
							.getValue("NUM")));
			if(equipmentCount>0){
				result.setResultHint("数据删除失败，原因为：当前间隔单元数据下面有未删除的设备数据");
				return result;
			}
			break;
		case station :
			int jgdyCount = Integer.parseInt(String
					.valueOf(genericDao
							.getDataIntoDataTable(stationCanDelSql,
									new Object[] { objId }).getRows().get(0)
							.getValue("NUM")));
			if(jgdyCount>0){
				result.setResultHint("数据删除失败，原因为：当前站房数据下面有未删除的间隔单元数据");
				return result;
			}
			break;
		}
		result = sigleBDService.delete(new String[]{objId}, appId, classId, user.getPersId());
		return result;
	}
}
