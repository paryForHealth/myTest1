/*
 * Copyright (C), 北京中恒博瑞数字电力技术有限公司，保留所有权利.
 * FileName: Constant.java
 * History：
 * <author>         <time>             <version>      <desc>
 *   ghz     2016年7月4日下午3:07:39        V1.0         TODO
 */
package com.jb.equipment.constants;

/**
 * 设备类型常量类
 * @Package: com.jb.equipment.constants<br>
 * @ClassName: Constant<br>
 * @Description: 设备类型常量类<br>
 */
public class Constant {
	
    /**
     * 设备维护方式
     */
    public enum Equipment {
    	/** 站内设备 */
    	station, 
    	/** 线路设备 */
    	line;
    }
    
    public enum ChangeEquipmentStatus{
    	//发布
    	publish,
    	//退役
    	retire;
    }
    
    /**
     * 
     * @Package: com.jb.equipment.constants<br>
     * @ClassName: StationEquipmentType<br>
     * @Description: 站房设备类型<br>
     */
    public enum StationEquipmentType{
    	//站房
    	station,
    	//设备
    	equiment,
    	//间隔单元
    	jgdy
    }
    
    /** 运维单位  */
    public static final String OPERATION_DEPT = "opeDept";
	
    /** 运营商 */
    public static final String DEPT_OPERATER = "1501601";
	
    /** 最终用户 */
    public static final String DEPT_USER = "1501602";

}
