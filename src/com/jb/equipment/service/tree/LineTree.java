/*
 * Copyright (C), 北京中恒博瑞数字电力技术有限公司，保留所有权利.
 * FileName: LineTree.java
 * History：
 * <author>         <time>             <version>      <desc>
 *   ghz     2016年7月4日下午4:19:20        V1.0         TODO
 */
package com.jb.equipment.service.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.jb.model.UserModel;
import com.jb.ui.model.FilterModel;
import com.jb.ui.model.TreeNodeModel;

/**
 * 线路设备维护导航树
 * @Package: com.jb.equipment.service.tree<br>
 * @ClassName: LineTree<br>
 * @Description: 线路设备维护导航树<br>
 */
@Service("lineTree")
public class LineTree extends StationTree {


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
		}// 第二层节点获取具体的线路类型
		else if (queryModel.getDepth() == 2) {
			getStationByType(queryModel, model);
		}// 第三层获取具体的站房
		else if (queryModel.getDepth() == 3) {
			getLine(queryModel, model);
		}//最后以及获取线路
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
	private void getLine(TreeNodeModel queryModel,
			List<TreeNodeModel> model) {
		StringBuilder sql = new StringBuilder();
		// 查询对应的间隔单元
		sql.append("select guid,XLMC from ts_sbtz.TB_SB_SPDSB_XL t ")
				.append(" where t.QDDZ=? ")
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

	@Override
	public Map<Object, String> putDisplay(Set selectedValueSet) {
		// TODO Auto-generated method stub
		return null;
	}

}
