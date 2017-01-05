package com.jb.equipment.service.tree;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jb.dao.GenericDao;
import com.jb.data.DataColumn;
import com.jb.data.DataTable;
import com.jb.equipment.model.Equipment;
import com.jb.model.UserModel;
import com.jb.ui.model.FilterModel;
import com.jb.ui.model.QureyGridModel;
import com.jb.ui.service.EntityOperationServiceAdapter;
import com.jb.util.platform.ResourceManager;

@Service("EquipmentMantenanceService")
public class EquipmentMantenanceService extends
		EntityOperationServiceAdapter<Equipment> {

	/**
	 * 间隔单元下设备汇总查询sql
	 */
	public static final String GETEQUOFDYDJ_SQLFLAG = "getEquOfDydj";
	
	public static final String GETEQUOFDYDJ_SQLTOTAL ="getEquOfDydjTotal";

	/**
	 * 间隔单元查询标志
	 */
	public static final String JGDY = "jgdy";

	/*
	 * 注入通用数据仓库
	 */
	@Resource(name = "genericDao")
	protected GenericDao genericDao;

	/*
	 * 查询所要展示的到页面的设备类型
	 */
	public DataTable query(QureyGridModel qureyGridModel, UserModel userModel) {
		// 获取间隔单元查询信息
		FilterModel filterModel = qureyGridModel.findFilter(JGDY);
		int start=qureyGridModel.getStart();
		int limit=qureyGridModel.getLimit();
		
		//查询总数
		String sql = ResourceManager.getInstance()
		.getDBSS(GETEQUOFDYDJ_SQLTOTAL);
		DataTable tTable = this.genericDao.getDataIntoDataTable(sql,
				new Object[] { filterModel.getValue() });
		int total=Integer.parseInt(tTable.getRow(0).getValue(0).toString());
		// 间隔下的设备查询sql
		sql = ResourceManager.getInstance()
				.getDBSS(GETEQUOFDYDJ_SQLFLAG);
		DataTable dataTable = this.genericDao.getDataIntoDataTable(sql,
				new Object[] { filterModel.getValue(),start,limit });

		dataTable.setTotal(total);
		
		// 设置隐藏以及标题名称
		DataColumn column = dataTable.getColumn("GUID");
		column.setHidden(true);
		dataTable.getColumn("SBMC").setCaption("设备名称");
		dataTable.getColumn("SBLX").setHidden(true);
		dataTable.getColumn("SBFLMC").setCaption("设备类型");
		column = dataTable.getColumn("DYDJ");
		column.setHidden(true);
		column = dataTable.getColumn("DYDJ_DSPVALUE");
		column.setCaption("电压等级");
		column = dataTable.getColumn("JGDY");
		column.setHidden(true);
		column = dataTable.getColumn("JGDY_DSPVALUE");
		column.setCaption("所属单元");
		column = dataTable.getColumn("TYRQ");
		column.setCaption("投运日期");
		column = dataTable.getColumn("YXZT");
		column.setHidden(true);
		column = dataTable.getColumn("YXZT_DSPVALUE");
		column.setCaption("运行状态");
		column = dataTable.getColumn("SJZT");
		column.setHidden(true);
		column = dataTable.getColumn("SJZT_DSPVALUE");
		column.setCaption("数据状态");
		column = dataTable.getColumn("SSZF");
		column.setHidden(true);
		column = dataTable.getColumn("YWDW");
		column.setHidden(true);
		column = dataTable.getColumn("YWDW_DSPVALUE");
		column.setCaption("运维单位");
		column = dataTable.getColumn("APPID");
		column.setHidden(true);
		column = dataTable.getColumn("CLSID");
		column.setHidden(true);
		return dataTable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jb.ui.service.EntityOperationServiceAdapter#batchDelete(java.util
	 * .List, com.jb.model.UserModel)
	 */
	@SuppressWarnings("unchecked")
	public void batchDelete(List<Equipment> equipments,
			com.jb.model.UserModel userModel) {
		List<String> sqls = new ArrayList<String>();
		for (Equipment equipment : equipments) {
			
			String querySql = " select CLS_TABSCHEMA,CLS_TABNAME from us_sys.tb_model_cls where guid=?";
			List<Object[]> list = (List<Object[]>) this.genericDao.getDataWithSQL(querySql, new Object[]{equipment.getClsID()});
			String schema = list.get(0)[0].toString();
			String tableName = list.get(0)[1].toString();
			StringBuilder sql = new StringBuilder();
			sql.append(" delete from  ").append(schema).append(".").append(tableName)
					.append(" where guid='")
					.append(equipment.getGuid()).append("'");
			sqls.add(sql.toString());
		}
		this.genericDao.BatchExecuteUpdate(sqls);
	}
}
