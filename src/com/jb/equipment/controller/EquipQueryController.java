/*
 * Copyright (C), 北京中恒博瑞数字电力技术有限公司，保留所有权利.
 * FileName: EquipQueryController.java
 * History：
 * <author>         <time>             <version>      <desc>
 *   高新星                         2016年8月8日 上午10:13:36      V1.0          
*/
package com.jb.equipment.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jb.core.constant.ICnsView;
import com.jb.data.InvokeResult;
import com.jb.equipment.service.EquipQueryService;
import com.jb.f1.kernel.util.ThreadLocalUtils;
import com.jb.model.UserModel;
import com.jb.util.json.JsonBinder;

/**
 * 说明：
 */
@Controller
@RequestMapping("equipQuery")
public class EquipQueryController {
	@Resource(name="equipQueryService")
	private EquipQueryService queryService;
	
	/**
	 * 判断是否是最终用户
	 * @param um
	 * @return
	 */
	@RequestMapping("getSelfDeptType.do")
	@ResponseBody
	public String getSelfDeptType(){
		//获取当前登陆人信息
		HttpSession session = (HttpSession) ThreadLocalUtils
				.getObjectFromThreadLocal("session");
		UserModel userModel = (UserModel) session
				.getAttribute(ICnsView.LOGIN_USERMODEL);
		InvokeResult result = new InvokeResult();
		try{
			result.setSuccessful(true);
			Map<String,String> resultMap = queryService.getSelfDeptType(userModel);
			result.setResultValue(resultMap);
		}catch(Exception e){
			result.setSuccessful(false);
			result.setResultHint("数据库操作失败。");
		}
		return JsonBinder.getJsonBinder().toJson(result);
	}
	
	@RequestMapping("getColVal.do")
	@ResponseBody
	public String getColVal(String objId, String type){
		InvokeResult result = new InvokeResult();
		try{
			String resultStr = queryService.getColVal(objId, type);
			result.setSuccessful(true);
			result.setResultValue(resultStr);
		}catch(Exception e){
			result.setSuccessful(false);
			result.setResultHint("数据库操作失败。");
		}
		return JsonBinder.getJsonBinder().toJson(result);
	}
}
