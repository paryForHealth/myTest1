/*
 * Copyright (C), 北京中恒博瑞数字电力技术有限公司，保留所有权利.
 * FileName: LineProtalProgressDataController.java
 * History：
 * <author>         <time>             <version>      <desc>
 *   guowenchao   	 2016年7月8日下午4:21:21          V1.0      线路维护页面导航条步骤数据获取
 */
package com.jb.equipment.controller;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jb.data.InvokeResult;
import com.jb.equipment.service.LineSetupWithDeviceRelationService;
import com.jb.util.json.JsonBinder;

/**
 * @Package: com.jb.equipment.controller<br>
 * @ClassName: LineProtalProgressDataController<br>
 * @Description: TODO<br>
 */
@Controller("lineProtalProgressDataController")
@RequestMapping("/progressData")
public class LineProtalProgressDataController {

	private final static Logger log = Logger.getLogger(LineProtalProgressDataController.class);
	
	@Resource(name = "lineSetupWithDeviceRelationService")
	private LineSetupWithDeviceRelationService lineSetupWithDeviceRelationServices;
	
	@RequestMapping("getSetupBySetUpId.do")
	@ResponseBody
	public String getSetupBylineDataGuid(String lineDataGuid){
		InvokeResult result = new InvokeResult();
		try {
			String data = lineSetupWithDeviceRelationServices.queryByLineDataGuid(lineDataGuid);
			result.setResultValue(data);
			result.setSuccessful(true);
		} catch (Exception e) {
			result.setSuccessful(false);
			result.setResultHint("查询数据异常!");
		}
		return JsonBinder.getJsonBinder().toJson(result);
	}
}
