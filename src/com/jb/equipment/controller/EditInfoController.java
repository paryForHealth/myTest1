/*
 * Copyright (C), 北京中恒博瑞数字电力技术有限公司，保留所有权利.
 * FileName: EditInfoController.java
 * History：
 * <author>         <time>             <version>      <desc>
 *   ghz     2016年7月6日下午2:19:39        V1.0         TODO
 */
package com.jb.equipment.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jb.core.constant.ICnsView;
import com.jb.data.InvokeResult;
import com.jb.equipment.constants.Constant.StationEquipmentType;
import com.jb.equipment.service.EditInfoService;
import com.jb.f1.kernel.util.ThreadLocalUtils;
import com.jb.model.UserModel;
import com.jb.model.engine.SingleBDService;
import com.jb.util.json.JsonBinder;

/**
 * @Package: com.jb.equipment.controller<br>
 * @ClassName: EditInfoController<br>
 * @Description: 编辑信息交互控制器<br>
 */
@Controller("editInfoController")
@RequestMapping("/editInfo")
public class EditInfoController {

	/** 日志 */
	final static Logger log = Logger.getLogger(EditInfoController.class);
	
	/** 编辑信息获取 */
	@Resource(name = "editInfoService")
	private EditInfoService editInfoService;

	private static final String STATION_TYPE_LEVEL="2";
	private static final String STATION_LEVEL="3";
	private static final String JGDY_LEVEL="4";
	
	/**
	 * 获取设备编辑需要的类型信息
	 * 
	 * @Title: getDataSource
	 * @Description: 获取设备编辑需要的类型信息
	 * @param
	 * @return String
	 * @throws
	 */
	@RequestMapping("getEditInfo.do")
	@ResponseBody
	public String getDataSource() {
		InvokeResult invokeResult = new InvokeResult();
		invokeResult.setSuccessful(true);
		invokeResult.setResultValue(editInfoService.getEquEditInfo().toJson());
		return JsonBinder.getJsonBinder().toJson(invokeResult);
	}

	@RequestMapping("deleteEquiment.do")
	@ResponseBody
	public String deleteEquiment(String level,String classId,String appId,String objId){
		InvokeResult result = new InvokeResult();
		if(level.equals(JGDY_LEVEL)){//间隔单元节点，删除设备
			result = editInfoService.deleteEquipment(StationEquipmentType.equiment,appId,classId,objId);
		}
		if(level.equals(STATION_LEVEL)){//站房节点，删除间隔单元
			result = editInfoService.deleteEquipment(StationEquipmentType.jgdy, appId, classId, objId);
		}
		if(level.equals(STATION_TYPE_LEVEL)){//站房类型节点，删除站房
			result = editInfoService.deleteEquipment(StationEquipmentType.station, appId, classId, objId);
		}
		return JsonBinder.getJsonBinder().toJson(result);
	}
}
