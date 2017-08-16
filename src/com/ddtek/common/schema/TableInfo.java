package com.ddtek.common.schema;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TableInfo {

	private String name;
	private String fieldName;
	private String userData;
	
	Map<String,ColumnInfo> columns = new LinkedHashMap<String,ColumnInfo>();	
	List<StatsInfo> stats = new ArrayList<StatsInfo>();
	List<PkFkInfo> pkFks = new ArrayList<PkFkInfo>();

	public TableInfo(String name, String fieldName, String userData) {
		super();
		
		this.name = name == null? null : name.toUpperCase();
		this.fieldName = fieldName;
		this.userData = userData;
	}

	
	public void addColumnInfo( ColumnInfo cInfo){
		this.columns.put(cInfo.getName().toUpperCase(), cInfo);
	}

	public void addStatsInfo( StatsInfo sInfo){
		this.stats.add(sInfo);
	}

	public void addPkFkInfo( PkFkInfo pkFkInfo){
		this.pkFks.add(pkFkInfo);
	}

	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see oajava.ws.TableInfo#setName(java.lang.String)
	 */
	
	public void setName(String name) {
		this.name = name == null? null : name.toUpperCase();
	}

	/* (non-Javadoc)
	 * @see oajava.ws.TableInfo#getColumns()
	 */
	
	public Map<String,ColumnInfo> getColumns() {
		return columns;
	}

	/* (non-Javadoc)
	 * @see oajava.ws.TableInfo#setColumns(java.util.List)
	 */
	
	public void setColumns(Map<String,ColumnInfo> columns) {
		this.columns.putAll(columns);
	}


	
	
	public String getFieldName() {
		return fieldName;
	}

	
	public String getUserData() {
		return userData;
	}

	
	public void setFieldName(String name) {
		this.fieldName = name;		
	}

	
	public void setUserData(String userdata) {
		this.userData = userdata;
	}

	
	
	public List<StatsInfo> getStats() {
		return this.stats;
	}
	
	public List<PkFkInfo> getPkFks() {
		return this.pkFks;
	}
	


	public void setPkFks(List<PkFkInfo> pkFks) {
		this.pkFks.addAll(pkFks);		
	}

	public void setStats(List<StatsInfo> stats) {
		this.stats.addAll(stats);
	}

}
