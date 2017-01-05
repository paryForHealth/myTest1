/*
 * Copyright (C), 北京中恒博瑞数字电力技术有限公司，保留所有权利.
 * FileName: PublishAndRetireController.java
 * History：
 * <author>         <time>             <version>      <desc>
 *   guowenchao   	 2016年7月8日下午6:08:00          V1.0        发布退役控制器
 */
package com.jb.equipment.controller;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jb.data.InvokeResult;
import com.jb.equipment.service.PublishAndRetireService;
import com.jb.util.json.JsonBinder;

/**
 * @Package: com.jb.equipment.controller
 * @ClassName: PublishAndRetireController
 * @Description: 发布退役控制器
 */
@Controller("publishAndRetireController")
@RequestMapping("/progressData")
public class PublishAndRetireController {

	private final static Logger log = Logger
			.getLogger(PublishAndRetireController.class);

	/** json转换工具 */
	private final static JsonBinder jsonBinder = JsonBinder.getJsonBinder();

	@Resource(name = "publishAndRetireService")
	private PublishAndRetireService service;

	/**
	 * 
	 * @Title: publish
	 * @Description: 发布
	 * @param
	 * @return InvokeResult
	 * @throws
	 */
	@RequestMapping("publish.do")
	@ResponseBody
	public String publish(String classId, String guid) throws Exception {
		return jsonBinder.toJson(service.publish(classId, guid));
	}

	/**
	 * 
	 * @Title: retire
	 * @Description: 退役
	 * @param
	 * @return InvokeResult
	 * @throws
	 */
	@RequestMapping("retire.do")
	@ResponseBody
	public String retire(String classId, String guid) throws Exception {
		return jsonBinder.toJson(service.retire(classId, guid));
	}
}
