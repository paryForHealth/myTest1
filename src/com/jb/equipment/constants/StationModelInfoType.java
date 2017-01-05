/*
 * Copyright (C), 北京中恒博瑞数字电力技术有限公司，保留所有权利.
 * FileName: StationModelInfoType.java
 * History：
 * <author>         <time>             <version>      <desc>
 *   guowenchao   	 2016年8月11日上午10:01:31          V1.0         TODO
 */
package com.jb.equipment.constants;

/**
 * @Package: com.jb.equipment.constants<br>
 * @ClassName: StationModelInfoType<br>
 * @Description: TODO<br>
 */
public enum StationModelInfoType {
    	ZFLX_BD("5969D58A-6FE8-49C8-9219-18D356E898D0-01325","11166049-0B22-443E-9CAB-133FF71063CB"), //变电站
    	ZFLX_KG("8C4E7A55-F718-4339-A90C-AE99780AAB4E-00136","11166049-0B22-443E-9CAB-133FF71063CB"), //开关站
    	ZFLX_DC("",""), //电厂
    	ZFLX_HL("",""), //换流站
    	ZFLX_CB("",""), //串补站
    	ZFLX_PD("8C4E7A55-F718-4339-A90C-AE99780AAB4E-00248","11166049-0B22-443E-9CAB-133FF71063CB"), //配电室
    	ZFLX_HW("8C4E7A55-F718-4339-A90C-AE99780AAB4E-00368","11166049-0B22-443E-9CAB-133FF71063CB"), //环网柜
    	ZFLX_XB("8C4E7A55-F718-4339-A90C-AE99780AAB4E-00006","11166049-0B22-443E-9CAB-133FF71063CB"), //箱式变电站
    	ZFLX_FZ("8C4E7A55-F718-4339-A90C-AE99780AAB4E-00498","11166049-0B22-443E-9CAB-133FF71063CB"), //电缆分支箱
    	ZFLX_KB("",""); //开闭站
    private String stationClsId;
    private String stationAppId;    
    
	private StationModelInfoType(String clsId,String appId) {
		stationClsId =clsId;
		stationAppId = appId;
	}
	
	public String getStationClsId(){
		return stationClsId;
	}
	
	public String getStationAppId(){
		return stationAppId;
	}
	
	public static StationModelInfoType getStationModelInfoType(String stationClsId){
		for(StationModelInfoType type : StationModelInfoType.values()){
			if(type.getStationClsId().equals(stationClsId)){
				return type;
			}
		}
		return null;
	}
	
}
