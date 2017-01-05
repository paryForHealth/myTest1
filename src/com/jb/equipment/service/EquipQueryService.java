/*
 * Copyright (C), 北京中恒博瑞数字电力技术有限公司，保留所有权利.
 * FileName: EquipQueryService.java
 * History：
 * <author>         <time>             <version>      <desc>
 *   高新星                         2016年8月8日 上午9:33:26      V1.0          
*/
package com.jb.equipment.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jb.dao.GenericDao;
import com.jb.data.DataRow;
import com.jb.data.DataTable;
import com.jb.equipment.constants.Constant;
import com.jb.equipment.constants.TableInfo;
import com.jb.model.UserModel;
import com.jb.util.platform.ResourceManager;

/**
 * 说明：设备信息查询服务
 */
@Service("equipQueryService")
public class EquipQueryService {
	@Resource(name = "genericDao")
	private GenericDao genericDao;
	/**
	 * 判断是否是最终用户
	 * @param um
	 * @return
	 */
	public Map<String,String> getSelfDeptType(UserModel um){
		boolean ifUser = false;
		StringBuilder query = new StringBuilder();
		query.append(ResourceManager.getInstance().getDBSS("getDeptInfo"));
		DataTable result = genericDao.getDataIntoDataTable(query.toString(), 
				new Object[]{um.getDeptRootID()});//传入该用户的顶级单位，查到该顶级单位的类别
		Map<String,String> map = new HashMap<String,String>();
		map.put("ISUSER", "false");
		if(result.getRows().size() > 0){
			String Nature = (String) result.getRow(0).getValue("NATURE");
			//如果是最终用户
			if(Constant.DEPT_USER.equals(Nature)){
				String xtdw = String.valueOf(result.getRow(0).getValue("XTDW"));
				map.put("ISUSER", "true");
				map.put("XTDW", xtdw);
			}
		}
		return map;
	}
	/**
	 * 获取某部门顶级部门的所有代维的单位
	 * @param rootId
	 * @return
	 */
	public DataTable allMaintenceDepts(String rootId){
		StringBuilder query = new StringBuilder();
		query.append(ResourceManager.getInstance().getDBSS(
				"getMaintenanceDepts"));
		DataTable result = genericDao.getDataIntoDataTable(query.toString(),
				new Object[] { rootId });
		return result;
	}
	
	public String getColumnStr(DataTable src, String columnName){
		String result = "";
		int i=0;
		for(DataRow dr : src.getRows()){
			if(i > 0 ){
				result+="','";
			}
			result += String.valueOf(dr.getValue(columnName));
			i++;
		}
		return "'"+result+"'";
	}
	
	/**
	 * 获取某表中某个字段
	 * @param objId 主键
	 * @param type 类型
	 * @return
	 */
	public String getColVal(String objId, String type){
		TableInfo table = null;
		//取间隔单元的电压等级
		if(type.equals("jgdy_dydj")){
			table = TableInfo.JGDY_DYDJ; 
		}
		String query = "select dydj from " + table.getTableName() + " where guid = ?";
		List<?> result = genericDao.getDataWithSQL(query, 
				new Object[]{ objId });
		if(result.size() > 0 && result.get(0)!=null){
			return result.get(0).toString();
		}else{
			return null;
		}
	}
}
