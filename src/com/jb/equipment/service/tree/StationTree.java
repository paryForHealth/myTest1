/*
 * Copyright (C), 北京中恒博瑞数字电力技术有限公司，保留所有权利.
 * FileName: StationTree.java
 * History：
 * <author>         <time>             <version>      <desc>
 *   ghz     2016年7月4日下午2:42:42        V1.0         TODO
 */
package com.jb.equipment.service.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jb.dao.GenericDao;
import com.jb.equipment.constants.Constant;
import com.jb.model.UserModel;
import com.jb.ui.model.FilterModel;
import com.jb.ui.model.TreeNodeModel;
import com.jb.ui.service.TreeService;

/**
 * 站房设备类型维护
 * 
 * @Package: com.jb.equipment.service.tree<br>
 * @ClassName: StationTree<br>
 * @Description: 站房设备类型维护<br>
 */
@Service("stationTree")
public class StationTree extends TreeService {
	/** 数据仓库 */
	@Resource(name = "genericDao")
	protected GenericDao genericDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jb.ui.service.TreeService#query(com.jb.ui.model.TreeNodeModel,
	 * java.util.List, com.jb.model.UserModel)
	 */
	@Override
	public List<TreeNodeModel> query(TreeNodeModel queryModel,
			List<FilterModel> filterModels, UserModel userModel) {

		// 当前登录系统用户
		List<TreeNodeModel> model = new ArrayList<TreeNodeModel>();

		/* 获取树信息 */
		if (queryModel.getDepth() == 0) {
			// 获取运维单位节点
			getMaintenanceDept(userModel, model);
		}// 第一级节点获取当前登录人所在单位
		else if (queryModel.getDepth() == 1) {
			getStationType(model);
		}// 第二层节点获取具体的站房分类
		else if (queryModel.getDepth() == 2) {
			getStationByType(queryModel, model);
		}// 第三层获取具体的站房
		else if (queryModel.getDepth() == 3) {
			getIntervals(queryModel, model);
		}
		return model;

	}

	/**
	 * @Title: getIntervals
	 * @Description: 获取间隔
	 * @param
	 * @return void
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	private void getIntervals(TreeNodeModel queryModel,
			List<TreeNodeModel> model) {
		StringBuilder sql = new StringBuilder();
		// 查询对应的间隔单元
		sql.append("select guid,BDZMC from ts_sbtz.tb_sb_bdsb_jgdy t ")
				.append(" where t.SSZF=? ")
				.append(" order by OBJ_DISPIDX desc");
		List<Object[]> list = (List<Object[]>) this.genericDao.getDataWithSQL(
				sql.toString(), new Object[] { queryModel.getId() });
		for (Object[] objects : list) {
			TreeNodeModel treeNode = new TreeNodeModel();
			treeNode.setSelected(true);
			treeNode.setId(objects[0].toString());// 节点id
			treeNode.setText(objects[1].toString());// 节点名称
			model.add(treeNode);
		}
	}

	/**
	 * @Title: getStationByType
	 * @Description: 通过运维单位以及变电站类型获取具体站房
	 * @param
	 * @return void
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	protected void getStationByType(TreeNodeModel queryModel,
			List<TreeNodeModel> model) {
		// 附加属性包含了运维单位信息
		Map<String, String> attrs = queryModel.getAttr();
		String opeDept = attrs.get(Constant.OPERATION_DEPT);// 运维单位

		StringBuilder sql = new StringBuilder();
		// 查询对应的站房分类
		sql.append("select guid,BDZMC from ts_sbtz.tb_sb_bdsb_dz t ")
				.append(" where t.YWDW=? and t.DZLX=? ")
				.append(" order by OBJ_DISPIDX desc");
		List<Object[]> list = (List<Object[]>) this.genericDao.getDataWithSQL(
				sql.toString(), new Object[] { opeDept, queryModel.getId() });
		for (Object[] objects : list) {
			TreeNodeModel treeNode = new TreeNodeModel();
			treeNode.setSelected(true);
			treeNode.setId(objects[0].toString());// 节点id
			treeNode.setText(objects[1].toString());// 节点名称
			model.add(treeNode);
		}
	}

	/**
	 * @Title: getStationType
	 * @Description: 获取站房分类
	 * @param
	 * @return void
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	protected void getStationType(List<TreeNodeModel> model) {
		StringBuilder sql = new StringBuilder();
		// 查询对应的站房分类
		sql.append("select code,code_name from us_sys.tb_sys_code t1 ").append(
				" where t1.LABEL_CODE='02030' order by code");
		List<Object[]> list = (List<Object[]>) this.genericDao
				.getDataWithSQL(sql.toString());
		for (Object[] objects : list) {
			TreeNodeModel treeNode = new TreeNodeModel();
			treeNode.setSelected(true);
			treeNode.setId(objects[0].toString());// 节点id
			treeNode.setText(objects[1].toString());// 节点名称
			// TODO 需要添加appid以及clsid
			model.add(treeNode);
		}
	}

	/**
	 * @Title: getMaintenanceDept
	 * @Description: 获取运维单位节点
	 * @param userModel
	 *            登陆用户模型
	 * @param model
	 *            树节点集合
	 * @return void
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	protected void getMaintenanceDept(UserModel userModel,
			List<TreeNodeModel> model) {
		StringBuilder sql = new StringBuilder();
		/* 查询运维单位的id及名称 */
		sql.append(
				" select t.DEPT_ID,t.DEPT_NAME from us_sys.tb_sys_department")
				.append(" t where t.DEPT_ID=? ");
		List<Object[]> list = (List<Object[]>) this.genericDao.getDataWithSQL(
				sql.toString(), new Object[] { userModel.getDeptRootID() });
		for (Object[] objects : list) {
			TreeNodeModel treeNode = new TreeNodeModel();
			treeNode.setSelected(true);
			treeNode.setId(objects[0].toString());// 节点id
			treeNode.setText(objects[1].toString());// 节点名称
			model.add(treeNode);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Object, String> putDisplay(Set selectedValueSet) {
		// TODO Auto-generated method stub
		return null;
	}

}
