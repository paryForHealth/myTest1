/*
 * Copyright (C), 北京中恒博瑞数字电力技术有限公司，保留所有权利.
 * FileName: EquipmentCodeController.java
 * History：
 * <author>         <time>             <version>      <desc>
 *   ghz     2016年7月11日下午8:51:46        V1.0         TODO
 */
package com.jb.equipment.controller;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jb.data.InvokeResult;
import com.jb.equipment.service.EquipmentCodeService;
import com.jb.util.json.JsonBinder;

/**
 * 设备编码控制器
 * @Package: com.jb.equipment.controller<br>
 * @ClassName: EquipmentCodeController<br>
 * @Description: 设备编码控制器<br>
 */
@Controller("equipmentCode")
@RequestMapping("/code")
public class EquipmentCodeController {
	
	
	/** 日志 */
	final static Logger log = Logger.getLogger(EditInfoController.class);

	/** 编辑信息获取 */
	@Resource(name = "equipmentCodeService")
	private EquipmentCodeService equipmentCodeService;

	/**
	 * 获取设备编辑需要的类型信息
	 * 
	 * @Title: getDataSource
	 * @Description: 获取设备编辑需要的类型信息
	 * @param
	 * @return String
	 * @throws
	 */
	@RequestMapping("getEquipmentCode.do")
	@ResponseBody
	public String getEquipmentCode(String clsID) {
		InvokeResult invokeResult = new InvokeResult();
		invokeResult.setSuccessful(true);
		invokeResult.setResultValue(equipmentCodeService.getEquipmentCode(clsID));
		return JsonBinder.getJsonBinder().toJson(invokeResult);
	}
	
	/**
	 * @Title: getDataSource
	 * @Description: 生成站房的设备编号
	 * @param
	 * @return String
	 * @throws
	 */
	@RequestMapping("getEquipmentCodeNoDate.do")
	@ResponseBody
	public String getEquipmentCodeNoDate(String zflx) {
		InvokeResult invokeResult = new InvokeResult();
		invokeResult.setSuccessful(true);
		invokeResult.setResultValue(equipmentCodeService.getEquipmentCodeNoDate(zflx));
		return JsonBinder.getJsonBinder().toJson(invokeResult);
	}
	
	/**
	 * 
	 * @Title: getStationInfoForCustomerEquipment
	 * @Description: 获取站房信息
	 * @param 
	 * @return String
	 * @throws
	 */
	@RequestMapping("getStationInfoForCustomerEquipment.do")
	@ResponseBody
	public String getStationInfoForCustomerEquipment(String objID) throws Exception{
		InvokeResult invokeResult = new InvokeResult();
		invokeResult.setResultValue(equipmentCodeService.getStationInfoForCustomer(objID));
		invokeResult.setSuccessful(true);
		return JsonBinder.getJsonBinder().toJson(invokeResult);
	}
	
	@RequestMapping("getEquipmentInfoForCustomer.do")
	@ResponseBody
	public String getEquipmentInfoForCustomer(String filterStr) throws Exception{
		InvokeResult invokeResult = new InvokeResult();
		invokeResult.setResultValue(equipmentCodeService.getEquipmentInfoForCustomer(filterStr));
		invokeResult.setSuccessful(true);
		return JsonBinder.getJsonBinder().toJson(invokeResult);
	}
}
