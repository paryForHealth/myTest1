package com.jb.equipment.constants;

/**
 * @author 高新星
 * 获取表名
 */
public enum TableInfo {
	JGDY_DYDJ("5969D58A-6FE8-49C8-9219-18D356E898D0-02198", "us_app.tb_sb_bdsb_jgdy", "DYDJ");
	private String clsId;
	private String tableName;
	private String colName;
	private TableInfo(String clsId, String tableName, String colName){
		this.clsId = clsId;
		this.tableName = tableName;
		this.colName = colName;
	}
	public String getTableName(){
		return this.tableName;
	}
	public String getClsId(){
		return this.clsId;
	}
	public String getColName(){
		return this.colName;
	}
}
