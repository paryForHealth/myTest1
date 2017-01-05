package com.jb.equipment.service.tree;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jb.config.util.PlatformConfigUtil;
import com.jb.dao.GenericDao;
import com.jb.data.DataColumn;
import com.jb.data.DataTable;
import com.jb.equipment.model.Equipment;
import com.jb.equipment.service.EquipQueryService;
import com.jb.model.UserModel;
import com.jb.ui.model.QureyGridModel;
import com.jb.ui.service.EntityOperationServiceAdapter;
import com.jb.ui.service.QueryFilterHelper;
import com.jb.util.platform.ResourceManager;

@Service("EquipmentQueryService")
public class EquipmentQueryService extends
		EntityOperationServiceAdapter<Equipment> {
	/*
	 * 注入通用数据仓库
	 */
	@Resource(name = "genericDao")
	protected GenericDao genericDao;
	
	@Resource(name="equipQueryService")
	private EquipQueryService queryService;
	/**
	 * 资产单位查询标志
	 */
	public static final String ZCDW = "ZCDW";
	/*
	 * 查询所要展示的到页面的设备类型
	 */
	public DataTable query(QureyGridModel qureyGridModel, UserModel userModel) {
		// 数据库类型
        String p_dbmsType = PlatformConfigUtil.getString("dbtype");
        // 查询过滤器
        QueryFilterHelper queryFilterHelper = new QueryFilterHelper(p_dbmsType);
		// 获取查询信息
		String sqlParams = queryFilterHelper.getSql(qureyGridModel
                .getFilterMap());
		int start=qureyGridModel.getStart();
		int limit=qureyGridModel.getLimit();
		
		// 设备查询sql
		StringBuilder query = new StringBuilder(ResourceManager.getInstance()
				.getDBSS("getEquList"));
		StringBuilder queryParams = new StringBuilder("");
		if(sqlParams.length() != 0){
			queryParams.append(" and ").append(sqlParams);
		}
		//只有在资产单位未传值的情况下才进行此项判断赋值
		if(qureyGridModel.findFilter(ZCDW) == null){
			//不是最终用户的情况下，才进行代维单位的选取作为数据的范围
			if(queryService.getSelfDeptType(userModel).get("ISUSER").equals("false")){
				DataTable allMainTainceDepts = queryService.allMaintenceDepts(userModel.getDeptRootID());
				sqlParams = queryService.getColumnStr(allMainTainceDepts, "XTDW");
				
				queryParams.append(" and zcdw in ("+sqlParams+")");
			}
		}
		query.append(queryParams);
		query.append(" order by zcdw, sblx, sbmc");
		DataTable dataTable = genericDao.exeSql(query.toString(), start, limit);
		int total = genericDao.getCountWithSQL(query.toString());
		dataTable.setTotal(total);
		
		buildColumn(dataTable);
		return dataTable;
	}
	
	/**
	 * @param dataTable
	 */
	public void buildColumn(DataTable dataTable) {
		// 设置隐藏以及标题名称
		dataTable.getColumn("GUID").setHidden(true);
		dataTable.getColumn("SBMC").setCaption("设备名称");
		dataTable.getColumn("SBLX").setHidden(true);
		dataTable.getColumn("SBFLMC").setCaption("设备类型");
		dataTable.getColumn("DYDJ").setHidden(true);
		dataTable.getColumn("DYDJ_DSPVALUE").setCaption("电压等级");
		dataTable.getColumn("JGDY").setHidden(true);
		dataTable.getColumn("JGDY_DSPVALUE").setCaption("所属单元");
		dataTable.getColumn("TYRQ").setCaption("投运日期");
		dataTable.getColumn("YXZT").setHidden(true);
		dataTable.getColumn("YXZT_DSPVALUE").setCaption("运行状态");
		dataTable.getColumn("SJZT").setHidden(true);
		dataTable.getColumn("SJZT_DSPVALUE").setCaption("数据状态");
		dataTable.getColumn("SSZF").setHidden(true);
		dataTable.getColumn("ZCDWMC").setCaption("资产单位");
		dataTable.getColumn("YWDW").setHidden(true);
		dataTable.getColumn("YWDW_DSPVALUE").setCaption("运维单位");
		dataTable.getColumn("APPID").setHidden(true);
		dataTable.getColumn("CLSID").setHidden(true);
		//追加列
		DataColumn dc = new DataColumn();
		dc.setCaption("详情");
		dc.setColumnName("XQ");
		dc.setAlign("center");
		dataTable.appendColumn(dc);
	}
}
