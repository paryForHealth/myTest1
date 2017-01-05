/*
 * Copyright (C), 北京中恒博瑞数字电力技术有限公司，保留所有权利.
 * FileName: EquipmentCodeService.java
 * History：
 * <author>         <time>             <version>      <desc>
 *   ghz     2016年7月11日上午11:14:05        V1.0         TODO
 */
package com.jb.equipment.service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jb.dao.GenericDao;
import com.jb.dao.data.SequenceManager;
import com.jb.data.DataTable;
import com.jb.equipment.constants.StationModelInfoType;
import com.jb.util.platform.ResourceManager;

/**
 * 设备编码服务
 * 
 * @Package: com.jb.equipment.service<br>
 * @ClassName: EquipmentCodeService<br>
 * @Description: 设备编码服务<br>
 */
@Service("equipmentCodeService")
public class EquipmentCodeService {

	/** 通用数据仓储 */
	@Resource(name = "genericDao")
	protected GenericDao genericDao;

	/** 编码中日期部分的格式 */
	public static final String DATA_FORMAT = "yyyyMMdd";

	/** 设备编码使用的序列 */
	public static final String SB_SEQ = "us_app.Equipment_dis";

	/** 设备分类 */
	public static final String SBFL = "sbfl";

	/** 获取类型对应设备类型 信息sql */
	public static final String GETCLSSBLX_SQL = "getClsTableInfo";
	
	/** 站房类型编码 */
	public static final String ZFLX_BD = "0203001"; //变电站
	public static final String ZFLX_KG = "0203002"; //开关站
	public static final String ZFLX_DC = "0203003"; //电厂
	public static final String ZFLX_HL = "0203004"; //换流站
	public static final String ZFLX_CB = "0203005"; //串补站
	public static final String ZFLX_PD = "0203006"; //配电室
	public static final String ZFLX_HW = "0203007"; //环网柜
	public static final String ZFLX_XB = "0203008"; //箱式变电站
	public static final String ZFLX_FZ = "0203009"; //电缆分支箱
	public static final String ZFLX_KB = "0203010"; //开闭站
		

	/**
	 * @Title: getEquipmentCode
	 * @Description: 获取设备编码
	 * @param clsId
	 *            设备类型
	 * @return String
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public String getEquipmentCode(String clsId) {

		String sblx = getSblx(clsId);

		// 获取流水号
		int seq = SequenceManager.getSingleton().getNextSeqValue(SB_SEQ);
		//如果没有这个序列则在mysql中初始化这个序列
		if(seq == 0){
			genericDao.exeSql("select initval('"+SB_SEQ+"',0,1)");
			seq = SequenceManager.getSingleton().getNextSeqValue(SB_SEQ);;
		}
		DecimalFormat df = new DecimalFormat("0000000000");
		return sblx + df.format(seq);
	}

	/**
	 * @Title: getSblx
	 * @Description: 得到设置类型
	 * @param 
	 * @return String
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	private String getSblx(String clsId) {
		/* 获取类型表信息 */
		String sql = ResourceManager.getInstance().getDBSS(GETCLSSBLX_SQL);
		List<Object> list = (List<Object>) this.genericDao.getDataWithSQL(sql,
				new Object[] { clsId });
		if (list.isEmpty()) {
			return "";
		}
		String sblx = list.get(0).toString();
		return sblx;
	}
	
	/**
	 * @Title: getEquipmentCodeNoDate
	 * @Description: 获取设备编码不带日期
	 * @param zflx 站房类型编码
	 * @return String
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public String getEquipmentCodeNoDate(String zflx) {

		// 获取流水号
		int seq = SequenceManager.getSingleton().getNextSeqValue(SB_SEQ);
		//如果没有这个序列则在mysql中初始化这个序列
		if(seq == 0){
			genericDao.exeSql("select initval('"+SB_SEQ+"',0,1)");
			seq = SequenceManager.getSingleton().getNextSeqValue(SB_SEQ);;
		}
		DecimalFormat df = new DecimalFormat("0000000000");
		return zflx + df.format(seq);
	}
	
	/**
	 * 
	 * @Title: getStationInfoForCustomer
	 * @Description: 获取站房信息
	 * @param 
	 * @return Map<String,Object>
	 * @throws
	 */
	public Map<String,String> getStationInfoForCustomer(String objID)throws Exception{
		String sql = ResourceManager.getInstance().getDBSS("getStationInfoForCustomer");
		DataTable table = genericDao.getDataIntoDataTable(sql,new Object[]{objID});
		String stationClsId= String.valueOf(table.getRows().get(0).getValue("REALCLS"));
		StationModelInfoType type = StationModelInfoType.getStationModelInfoType(stationClsId);
		Map<String,String> result = new HashMap<String,String>();
		result.put("CLSID",type.getStationClsId());
		result.put("APPID", type.getStationAppId());
		result.put("OBJID", objID);
		return result;
	}
	
	public Map<String,String> getEquipmentInfoForCustomer(String filterStr) throws Exception{
		String sql = ResourceManager.getInstance().getDBSS("getEquipmentInfoForCustomer");
		sql = sql.replace("${FILTER}", " AND "+filterStr);
		DataTable table = genericDao.getDataIntoDataTable(sql, new Object[]{});
		String clsId = String.valueOf(table.getRows().get(0).getValue("CLSID"));
		String appId = String.valueOf(table.getRows().get(0).getValue("APPID"));
		Map<String,String>result = new HashMap<String,String>();
		result.put("CLSID", clsId);
		result.put("APPID", appId);
		return result;
	}
}
