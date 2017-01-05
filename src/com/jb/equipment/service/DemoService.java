/*
 * Copyright (C), 北京中恒博瑞数字电力技术有限公司，保留所有权利.
 * FileName: DemoService.java
 * History：
 * <author>         <time>             <version>      <desc>
 *   ghz     2016年7月7日下午3:33:31        V1.0         TODO
 */
package com.jb.equipment.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jb.dao.GenericDao;
import com.jb.data.DataTable;
import com.jb.data.InvokeResult;
import com.jb.model.UserModel;
import com.jb.permission.model.ussys.TbSysRole;
import com.jb.ui.exception.EntityOperationException;
import com.jb.ui.model.QureyGridModel;
import com.jb.ui.service.EntityOperationServiceAdapter;

@Service("demoService")
public class DemoService extends EntityOperationServiceAdapter<TbSysRole>{
	
	/** 通用数据仓储 */
	@Resource(name = "genericDao")
	protected GenericDao genericDao;
	
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jb.component.ui.service.EntityOperationService#delete(com.jb.core
	 * .model.PersistClass, com.jb.core.model.UserModel)
	 */
	@SuppressWarnings("unchecked")
	public void delete(TbSysRole t, UserModel userModel) {
//		InvokeResult result = this.beforeDelete(t, userModel);
//		if (result.getSuccessful()) {
//			String idVlaue = getIdValue(t).toString();// 获取主键值
//			T clazz = (T) this.genericDao.getPo(t.getClass(), idVlaue);
//			if (clazz != null) {
//				this.genericDao.removePo(clazz);
//				this.afterDelete(clazz, userModel);
//			}
//		} else {
//			throw new EntityOperationException("删除错误", result.getResultHint());
//		}
	}
	
	public DataTable query(QureyGridModel qureyGridModel, UserModel userModel) {
		String sql = " select guid,bdzmc,YWDW,'8C4E7A55-F718-4339-A90C-AE99780AAB4E-00136' as CLSID,"
				+ "'CA6E8464-3D0E-45E9-86AE-EBE0B10BA38C' as APPID"
				+ " from us_app.tb_sb_bdsb_kgz "
				+ " union all "
				+ " select guid,bdzmc,YWDW,	'8C4E7A55-F718-4339-A90C-AE99780AAB4E-00248' AS CLSID,"
				+ " 'CA6E8464-3D0E-45E9-86AE-EBE0B10BA38C' AS APPID"
				+ " from us_app.tb_sb_bdsb_pds";
		return this.genericDao.getDataIntoDataTable(sql, new Object[]{});
	}

}
