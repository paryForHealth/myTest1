/*
 * Copyright (C), 北京中恒博瑞数字电力技术有限公司，保留所有权利.
 * FileName: PublicAndRetireService.java
 * History：
 * <author>         <time>             <version>      <desc>
 *   guowenchao   	 2016年7月8日下午5:33:35          V1.0      发布退役服务
 */
package com.jb.equipment.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.jb.dao.GenericDao;
import com.jb.data.DataRow;
import com.jb.data.DataRowCollection;
import com.jb.data.DataTable;
import com.jb.data.InvokeResult;
import com.jb.equipment.constants.Constant;
import com.jb.util.platform.ResourceManager;

/**
 * @Package: com.jb.equipment.service<br>
 * @ClassName: PublicAndRetireService<br>
 * @Description: 发布退役服务
 */
@Service("publishAndRetireService")
public class PublishAndRetireService {

	@Resource(name = "genericDao")
	private GenericDao genericDao;

	// SQL中表名占位符
	private static final String TABLE_NAME_SEAT = "${TABLENAME}";
	// SQL中关系字段占位符
	private static final String RELATIONCOLUMN = "${RELATIONCOLUMN}";
	// 子类类型ID
	private static final String CHILDCLS = "childClsId";
	// 子类数据主键
	private static final String CHILDGUID = "childGuid";
	/**
	 * 
	 * @Title: publish
	 * @Description: 发布服务
	 * @param
	 * @return InvokeResult
	 * @throws
	 */
	public InvokeResult publish(String classId, String guid) throws Exception {
		return changePublishRetireStatus(classId, guid,
				Constant.ChangeEquipmentStatus.publish);
	}

	/**
	 * 
	 * @Title: retire
	 * @Description: 退役服务
	 * @param
	 * @return InvokeResult
	 * @throws
	 */
	public InvokeResult retire(String classId, String guid) throws Exception {
		return changePublishRetireStatus(classId, guid,
				Constant.ChangeEquipmentStatus.retire);

	}

	/**
	 * 
	 * @Title: getChildInfo
	 * @Description: 获取子类信息
	 * @param 
	 * @return Map<String,String>
	 * @throws
	 */
	private List<Map<String, String>> getChildInfo(
			DataRowCollection childCollection) throws Exception {
		List<Map<String, String>> childInfos = new ArrayList<Map<String, String>>();
		for (int i = 0; i < childCollection.size(); i++) {// 迭代获取子类信息
			Map<String, String> childInfoMap = new HashMap<String, String>();
			DataRow row = childCollection.get(i);
			String childClsId = String.valueOf(row.getValue("CHILD"));
			String childTableName = getDataTableNameByClsId(childClsId);
			String fk = String.valueOf(row.getValue("RELATIONCOLUMN"));
			String getChildGuidSql = ResourceManager.getInstance()
					.getDBSS("getChlidReltaionGuid")
					.replace(RELATIONCOLUMN, fk)
					.replace(TABLE_NAME_SEAT, childTableName);
			DataTable childGuidResult = genericDao.getDataIntoDataTable(
					getChildGuidSql, new Object[] { childClsId });// 获取子类GUID
			DataRowCollection childGuidResultRows = childGuidResult.getRows();
			if (childGuidResultRows.size() != 0) {// 发布子类
				for (int j = 0; j < childGuidResultRows.size(); j++) {
					String childGuid = String.valueOf(childGuidResultRows
							.get(j).getValue("GUID"));
					childInfoMap.put(CHILDCLS, childClsId);
					childInfoMap.put(CHILDGUID, childGuid);
					childInfos.add(childInfoMap);// 组装子类 发布/退役对象信息
				}
			}
		}
		return childInfos;
	}
	
	/**
	 * 
	 * @Title: changePublishRetireStatus
	 * @Description: 根据发布退役操作状态更改相应设备状态
	 * @param
	 * @return InvokeResult
	 * @throws
	 */
	private InvokeResult changePublishRetireStatus(String classId, String guid,
			Constant.ChangeEquipmentStatus status) throws Exception {
		InvokeResult result = new InvokeResult();
		if (StringUtils.isBlank(classId) || StringUtils.isBlank(guid)) {
			result.setSuccessful(false);
			result.setResultHint("发布失败，类型ID或数据ID为空");
		}
		String tableName = getDataTableNameByClsId(classId);
		String resultHint = "发布成功！";
		//自己设备退役SQL
		StringBuilder selfSql = new StringBuilder("");
		//查询子类SQL
		String queryChildCls = ResourceManager.getInstance().getDBSS("queryClsRelation");
		DataTable relationResult = genericDao.getDataIntoDataTable(queryChildCls, new Object[]{classId});
		boolean haveChild = false;//拥有子类标识
		DataRowCollection  relationRows = relationResult.getRows();
		if(relationRows.size() != 0 ){
			haveChild = true;
		}
		switch (status) {
		case publish:
			selfSql.append(ResourceManager.getInstance().getDBSS("publishServer"));
//			if(haveChild){//将子类发布
//				List<Map<String, String>> childInfos = getChildInfo(relationRows);
//				if (childInfos.size() == 0)break;
//				for(Map<String,String> childInfo : childInfos){//迭代将子类发布
//					publish(childInfo.get(CHILDCLS),childInfo.get(CHILDGUID));
//				}
//			}
			break;
		case retire:
			resultHint = "退役成功";
			selfSql.append(ResourceManager.getInstance().getDBSS("retireServer"));
//			if(haveChild){//将子类退役
//				List<Map<String, String>> childInfos = getChildInfo(relationRows);
//				if (childInfos.size() == 0) break;
//				for(Map<String,String> childInfo : childInfos){//迭代将子类退役
//					retire(childInfo.get(CHILDCLS),childInfo.get(CHILDGUID));
//				}
//			}
			break;
		}
		try {
			genericDao.executeWithNativeSql(
					selfSql.toString().replace(TABLE_NAME_SEAT, tableName),
					new Object[] { guid });
			result.setSuccessful(true);
			result.setResultHint(resultHint);
		} catch (Exception e) {
			result.setSuccessful(true);
			result.setResultHint("系统异常！");
		}
		return result;
	}

	/**
	 * 
	 * @Title: getDataTableNameByClsId
	 * @Description: 根据clsID获取表名
	 * @param
	 * @return String
	 * @throws
	 */
	private String getDataTableNameByClsId(String classId) throws Exception {
		String sql = ResourceManager.getInstance().getDBSS(
				"selectTableNameByClSID");
		DataTable result = genericDao.getDataIntoDataTable(sql,
				new Object[] { classId });
		return String.valueOf(result.getRow(0).getValue("TABLENAME"));
	}
}
