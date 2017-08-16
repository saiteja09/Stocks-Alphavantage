package com.ddtek.common.ip;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.ddtek.common.schema.ColumnInfo;
import com.ddtek.common.schema.PkFkInfo;
import com.ddtek.common.schema.StatsInfo;
import com.ddtek.common.schema.TableInfo;
import oajava.sql.ip;
import oajava.sql.jdam;
import oajava.sql.schemaobj_column;
import oajava.sql.schemaobj_fkey;
import oajava.sql.schemaobj_pkey;
import oajava.sql.schemaobj_stat;
import oajava.sql.schemaobj_table;

public class RestIPHelper {
	
	/* Support array */
	public static final int[]   IP_SUPPORT_ARRAY = 
	{
			0,
			1, /* IP_SUPPORT_SELECT */
			0, /* IP_SUPPORT_INSERT */
			0, /* IP_SUPPORT_UPDATE */
			0, /* IP_SUPPORT_DELETE */
			1, /* IP_SUPPORT_SCHEMA - IP supports Schema Functions */
			0, /* IP_SUPPORT_PRIVILEGES  */
			1, /* IP_SUPPORT_OP_EQUAL */
			1, /* IP_SUPPORT_OP_NOT   */
			1, /* IP_SUPPORT_OP_GREATER */
			1, /* IP_SUPPORT_OP_SMALLER */
			1, /* IP_SUPPORT_OP_BETWEEN */
			1, /* IP_SUPPORT_OP_LIKE    */
			1, /* IP_SUPPORT_OP_NULL    */
			0, /* IP_SUPPORT_SELECT_FOR_UPDATE */
			0, /* IP_SUPPORT_START_QUERY */
			0, /* IP_SUPPORT_END_QUERY */
			1, /* IP_SUPPORT_UNION_CONDLIST */
			0, /* IP_SUPPORT_CREATE_TABLE */
			0, /* IP_SUPPORT_DROP_TABLE */
			0, /* IP_SUPPORT_CREATE_INDEX */
			0, /* IP_SUPPORT_DROP_INDEX */
			0, /* IP_SUPPORT_PROCEDURE */
			0, /* IP_SUPPORT_CREATE_VIEW */
			0, /* IP_SUPPORT_DROP_VIEW */
			0, /* IP_SUPPORT_QUERY_VIEW */
			0, /* IP_SUPPORT_CREATE_USER */
			0, /* IP_SUPPORT_DROP_USER */
			0, /* IP_SUPPORT_CREATE_ROLE */
			0, /* IP_SUPPORT_DROP_ROLE */
			0, /* IP_SUPPORT_GRANT */
			0, /* IP_SUPPORT_REVOKE */
			0,  /* IP_SUPPORT_PASSTHROUGH_QUERY */
			0,  /* IP_SUPPORT_NATIVE_COMMAND */
			0,  /* IP_SUPPORT_ALTER_TABLE */
			0,  /* IP_SUPPORT_BLOCK_JOIN */
			0,  /* IP_SUPPORT_XA */
			0,  /* IP_SUPPORT_QUERY_MODE_SELECTION */
			0,  /* IP_SUPPORT_VALIDATE_SCHEMAOBJECTS_IN_USE */
			1,  /* IP_SUPPORT_UNICODE_INFO */
			0,  /* Reserved for future use */
			0,  /* Reserved for future use */
			0,  /* Reserved for future use */
			0,  /* Reserved for future use */
			0,  /* Reserved for future use */
			0,  /* Reserved for future use */
			0,  /* Reserved for future use */
			0,  /* Reserved for future use */
			0,  /* Reserved for future use */
			0   /* Reserved for future use */
	};

	static boolean bAllowSchemaSearchPattern = false;
	
	/**
	 * Helper for ipGetInfo()
	 * @param iInfoType
	 * @param sessionContext
	 * @return
	 */
	String getIPInfo(int iInfoType, SessionContext sessionContext) {
		String str = null;
		switch (iInfoType) {
		case ip.IP_INFO_QUALIFIER_TERMW:
			/* return the Qualifier term used in Schema information */
			str = (String) sessionContext.getProperty(RestIPConstants.DEFAULT_SQL_DBMS_NAME);
			break;
	
		case ip.IP_INFO_OWNER_TERMW:
			/* return the Owner term used in Schema information */
			str = "owner";
			break;
	
		case ip.IP_INFO_QUALIFIER_NAMEW:
			str = (String) sessionContext.getProperty(RestIPConstants.CATALOG_NAME_PROP);
			break;
	
		case ip.IP_INFO_OWNER_NAMEW:
			/* we need to return information that matches schema */
			str = (String) sessionContext.getProperty(RestIPConstants.SCHEMA_NAME_PROP);
			break;
	
		case ip.IP_INFO_DS_INFO:
			str = "1";
			break;
	
		case ip.IP_INFO_SUPPORT_SCHEMA_SEARCH_PATTERN:
			str = (!RestIPHelper.bAllowSchemaSearchPattern) ? "0" : "1"; /* false/true */
			break;
	
		case ip.IP_INFO_SUPPORT_VALUE_FOR_RESULT_ALIAS:
		case ip.IP_INFO_VALIDATE_TABLE_WITH_OWNER:
			str = "0"; /* false */
			break;
	
		case ip.IP_INFO_FILTER_VIEWS_WITH_QUALIFIER_NAME:
		case ip.IP_INFO_CONVERT_NUMERIC_VAL:
		case ip.IP_INFO_TABLE_ROWSET_REPORT_MEMSIZE_LIMIT:
			str = "1"; /* true */
			break;
		default:			
			break;
		}
		return str;
	}

	void handleDAMSchemaOrCatalog(long pMemTree, int iType, long pList, Object pSearchObj, boolean schema, SessionContext sessionContext){
		schemaobj_table TableObj = new schemaobj_table();
		
		if (schema) {
			TableObj.SetObjInfo(null, RestIPConstants.SYSTEM_OWNER, null, null, null, null, null, null);
		} else {
			TableObj.SetObjInfo(RestIPConstants.SYSTEM_CATALOG, null, null, null, null, null, null, null);
		}
		jdam.dam_add_schemaobj(pMemTree, iType, pList, pSearchObj, TableObj);
		
		if(schema){
			String schemaOwner = (String) sessionContext.getProperty(RestIPConstants.DEFAULT_SCHEMA_NAME);
			TableObj.SetObjInfo(null, schemaOwner, null, null, null, null, null, null);
		} else {
			String catalogName = (String) sessionContext.getProperty(RestIPConstants.CATALOG_NAME_PROP);
			TableObj.SetObjInfo(catalogName, null, null, null, null, null, null, null);
		}
		jdam.dam_add_schemaobj(pMemTree, iType, pList, pSearchObj, TableObj);
	}

	void handleTableType(long pMemTree, int iType, long pList, Object pSearchObj) {
		schemaobj_table TableObj = new schemaobj_table();
		
		String[] typeNames = {"SYSTEM TABLE", "TABLE", "VIEW"};
	
		for(String typeName : typeNames){
			TableObj.SetObjInfo(null, null, null, typeName, null, null, null, null);
			jdam.dam_add_schemaobj(pMemTree, iType, pList, pSearchObj, TableObj);
		}
	}

	void handleTable(long pMemTree, int iType, long pList, Object pSearchObj, SessionContext sessionContext) {
		schemaobj_table pTableSearchObj = (schemaobj_table) pSearchObj;
		String catalogName = (String) sessionContext.getProperty(RestIPConstants.CATALOG_NAME_PROP);
		String schemaOwner = (String) sessionContext.getProperty(RestIPConstants.SCHEMA_NAME_PROP);
		if (pTableSearchObj != null && pTableSearchObj.getTableName() != null) {
			String tableName = pTableSearchObj.getTableName();
			jdam.trace(sessionContext.getDam_Conn_Handle(), ip.UL_TM_MAJOR_EV, "Dynamic Schema  of table:<" + pTableSearchObj.getTableQualifier() + "."
					+ pTableSearchObj.getTableOwner() + "." + tableName + "> is being requested\n");
	
			TableInfo tableInfo = sessionContext.getSchemaDescriptor().getTableInfo(tableName);
			if (tableInfo != null && IsMatchingTable(pTableSearchObj, catalogName, schemaOwner, tableName)) {
				schemaobj_table TableObj = new schemaobj_table();
				TableObj.SetObjInfo(catalogName, schemaOwner, tableInfo.getName(), "TABLE", null, null, null, tableInfo.getName());
				jdam.dam_add_schemaobj(pMemTree, iType, pList, pSearchObj, TableObj);
			}
	
		} else {
			jdam.trace(sessionContext.getDam_Conn_Handle(), ip.UL_TM_MAJOR_EV, "Dynamic Schema for all tables is being requested\n");
	
			Set<String> tables = sessionContext.getSchemaDescriptor().getTableNameSet();
			for (String tableName : tables) {
				schemaobj_table TableObj = new schemaobj_table();
				TableInfo tableInfo = sessionContext.getSchemaDescriptor().getTableInfo(tableName);
				TableObj.SetObjInfo(catalogName, schemaOwner, tableInfo.getName(), "TABLE", null, null, null, tableInfo.getName());
				jdam.dam_add_schemaobj(pMemTree, iType, pList, pSearchObj, TableObj);
			}
	
		}
	}

	void handleColumn(long pMemTree, int iType, long pList, Object pSearchObj, SessionContext sessionContext) {
		schemaobj_column pColSearchObj = (schemaobj_column) pSearchObj;
		String catalogName = (String) sessionContext.getProperty(RestIPConstants.CATALOG_NAME_PROP);
		String schemaOwner = (String) sessionContext.getProperty(RestIPConstants.SCHEMA_NAME_PROP);
	
		if (pColSearchObj != null && pColSearchObj.getTableName() != null) {
			String tableName = pColSearchObj.getTableName();
			jdam.trace(sessionContext.getDam_Conn_Handle(), ip.UL_TM_MAJOR_EV, "Dynamic Schema for column <" + pColSearchObj.getColumnName() + "> of table:<"
					+ pColSearchObj.getTableQualifier() + "." + pColSearchObj.getTableOwner() + "." + tableName
					+ "> is being requested\n");
	
			if (IsMatchingColumn(pColSearchObj, catalogName, schemaOwner, tableName)) {
				TableInfo tableInfo;
				tableInfo = sessionContext.getSchemaDescriptor().getTableInfo(tableName);
				addColumnsToDAM(tableInfo, pMemTree, iType, pList, pSearchObj, sessionContext);
			}
		} else {
			jdam.trace(sessionContext.getDam_Conn_Handle(), ip.UL_TM_MAJOR_EV, "Dynamic Schema for all columns of all tables is being requested\n");
	
			Set<String> tables;
			tables = sessionContext.getSchemaDescriptor().getTableNameSet();
			for (String tableName : tables) {
				TableInfo tableInfo;
				tableInfo = sessionContext.getSchemaDescriptor().getTableInfo(tableName);
				addColumnsToDAM(tableInfo, pMemTree, iType, pList, pSearchObj, sessionContext);
			}
		}
	}

	void addColumnsToDAM(TableInfo tableInfo, long pMemTree, int iType, long pList, Object pSearchObj, SessionContext sessionContext) {
		Map<String,ColumnInfo> columnInfoList = (LinkedHashMap<String,ColumnInfo>) tableInfo.getColumns();
		String catalogName = (String) sessionContext.getProperty(RestIPConstants.CATALOG_NAME_PROP);
		String schemaOwner = (String) sessionContext.getProperty(RestIPConstants.SCHEMA_NAME_PROP);
	
		if (columnInfoList == null) {
			jdam.trace(sessionContext.getDam_Conn_Handle(), ip.UL_TM_ERRORS, "Couldn't find columns information for table " + tableInfo.getName() + "\n");
		} else {
			
			Set<Entry<String, ColumnInfo>> set =  columnInfoList.entrySet();

			for ( Entry<String, ColumnInfo> entry: set){
				
				ColumnInfo colInfo =  entry.getValue();
				
				schemaobj_column ColumnObj = new schemaobj_column();
				ColumnObj.SetObjInfo(catalogName, schemaOwner, tableInfo.getName(), colInfo.getName(), colInfo.getDataType(),
						colInfo.getTypeName(), colInfo.getCharMaxlength(), colInfo.getNumericPrecision(),
						colInfo.getNumericPrecisionRadix(), colInfo.getNumericScale(), colInfo.getNullable(), colInfo.getScope(),
						colInfo.getUserdata(), colInfo.getOperatorSupport(), colInfo.getPseudoColumn(), colInfo.getColumnType(),
						colInfo.getRemarks());
				
				jdam.dam_add_schemaobj(pMemTree, iType, pList, pSearchObj, ColumnObj);
			}
		}
	}

	boolean IsMatchingTable(schemaobj_table pSearchObj, String table_qualifier, String table_owner, String table_name) {
		if (pSearchObj == null)
			return true;
		boolean bSearchPattern = !(!RestIPHelper.bAllowSchemaSearchPattern || (jdam.dam_isSearchPatternObject(pSearchObj) == 0));
	
		if (!bSearchPattern) {
			/* match the search pattern */
			if ((pSearchObj.getTableQualifier() != null) && !pSearchObj.getTableQualifier().equalsIgnoreCase(table_qualifier))
				return false;
			if ((pSearchObj.getTableOwner() != null) && !pSearchObj.getTableOwner().equalsIgnoreCase(table_owner))
				return false;
			if ((pSearchObj.getTableName() != null) && !pSearchObj.getTableName().equalsIgnoreCase(table_name))
				return false;
		} else {
			/* match the search pattern */
			if ((pSearchObj.getTableQualifier() != null)
					&& jdam.dam_strlikecmp(pSearchObj.getTableQualifier(), table_qualifier) != 0)
				return false;
			if ((pSearchObj.getTableOwner() != null) && jdam.dam_strlikecmp(pSearchObj.getTableOwner(), table_owner) != 0)
				return false;
			if ((pSearchObj.getTableName() != null) && jdam.dam_strlikecmp(pSearchObj.getTableName(), table_name) != 0)
				return false;
		}
	
		return true;
	}

	boolean IsMatchingColumn(schemaobj_column pSearchObj, String table_qualifier, String table_owner, String table_name) {
		if (pSearchObj == null)
			return true;
		boolean bSearchPattern = !(!RestIPHelper.bAllowSchemaSearchPattern || (jdam.dam_isSearchPatternObject(pSearchObj) == 0));
	
		if (!bSearchPattern) {
			/* match the search pattern */
			if ((pSearchObj.getTableQualifier() != null) && !pSearchObj.getTableQualifier().equalsIgnoreCase(table_qualifier))
				return false;
			if ((pSearchObj.getTableOwner() != null) && !pSearchObj.getTableOwner().equalsIgnoreCase(table_owner))
				return false;
			if ((pSearchObj.getTableName() != null) && !pSearchObj.getTableName().equalsIgnoreCase(table_name))
				return false;
		} else {
			/* match the search pattern */
			if ((pSearchObj.getTableQualifier() != null)
					&& jdam.dam_strlikecmp(pSearchObj.getTableQualifier(), table_qualifier) != 0)
				return false;
			if ((pSearchObj.getTableOwner() != null) && jdam.dam_strlikecmp(pSearchObj.getTableOwner(), table_owner) != 0)
				return false;
			if ((pSearchObj.getTableName() != null) && jdam.dam_strlikecmp(pSearchObj.getTableName(), table_name) != 0)
				return false;
		}
	
		return true;
	}

	void handlePKey(long pMemTree, int iType, long pList, Object pSearchObj, SessionContext sessionContext) {
		schemaobj_pkey pSearchPkeyObj = null;
		pSearchPkeyObj = (schemaobj_pkey) pSearchObj;
	
		if (pSearchPkeyObj != null && pSearchPkeyObj.getPKTableName() != null) {
			jdam.trace(
					sessionContext.getDam_Conn_Handle(),
					ip.UL_TM_MAJOR_EV,
					"Dynamic Schema for PrimaryKeys for PKtable:<" + pSearchPkeyObj.getPKTableQualifier() + "."
							+ pSearchPkeyObj.getPKTableOwner() + "." + pSearchPkeyObj.getPKTableName() + "> is being requested\n");
			
			String tableName = pSearchPkeyObj.getPKTableName();
			// FIXME
			// if (IsMatching???(pSearchPkeyObj, PKEY_CATALOG_NAME, PKEY_USER_NAME, tableName)) {
			TableInfo tableInfo = sessionContext.getSchemaDescriptor().getTableInfo(tableName);
			addPrimaryKeyInfoToDAM(tableInfo, pMemTree, iType, pList, pSearchObj, sessionContext);
		} else {
			jdam.trace(sessionContext.getDam_Conn_Handle(), ip.UL_TM_MAJOR_EV, "Dynamic Schema for all PRIMARY KEYS is being requested\n");

			for (String tableName : sessionContext.getSchemaDescriptor().getTableNameSet()) {
				TableInfo tableInfo;
				tableInfo = sessionContext.getSchemaDescriptor().getTableInfo(tableName);
				addPrimaryKeyInfoToDAM(tableInfo, pMemTree, iType, pList, pSearchObj, sessionContext);
			}
		}
	}

	void handleFkey(long pMemTree, int iType, long pList, Object pSearchObj, SessionContext sessionContext) {
		schemaobj_fkey pSearchFkeyObj = null;
		pSearchFkeyObj = (schemaobj_fkey) pSearchObj;
	
		if (pSearchFkeyObj != null && pSearchFkeyObj.getFKTableName() != null) {
			jdam.trace(
					sessionContext.getDam_Conn_Handle(),
					ip.UL_TM_MAJOR_EV,
					"Dynamic Schema for ForeignKeys for PKtable:<" + pSearchFkeyObj.getPKTableQualifier() + "."
							+ pSearchFkeyObj.getPKTableOwner() + "." + pSearchFkeyObj.getPKTableName() + "> and FKtable:<"
							+ pSearchFkeyObj.getFKTableQualifier() + "." + pSearchFkeyObj.getFKTableOwner() + "."
							+ pSearchFkeyObj.getFKTableName() + "> is being requested\n");
			String tableName = pSearchFkeyObj.getFKTableName();
	
			// FIXME
			// if (IsMatching???(pSearchFkeyObj, FKEY_CATALOG_NAME, FKEY_USER_NAME, tableName)) {
			TableInfo tableInfo;
			tableInfo = sessionContext.getSchemaDescriptor().getTableInfo(tableName);
	
			addForeignKeyInfoToDAM(tableInfo, pMemTree, iType, pList, pSearchObj, sessionContext);
		} else {
			jdam.trace(sessionContext.getDam_Conn_Handle(), ip.UL_TM_MAJOR_EV, "Dynamic Schema for all FKEYS is being requested\n");
			Set<String> tables;
			tables = sessionContext.getSchemaDescriptor().getTableNameSet();
			for (String tableName : tables) {
				TableInfo tableInfo;
				tableInfo = sessionContext.getSchemaDescriptor().getTableInfo(tableName);
				addForeignKeyInfoToDAM(tableInfo, pMemTree, iType, pList, pSearchObj, sessionContext);
			}
		}
	}

	void handleStat(long pMemTree, int iType, long pList, Object pSearchObj, SessionContext sessionContext) {
		schemaobj_stat pSearchStatObj = null;
		pSearchStatObj = (schemaobj_stat) pSearchObj;
	
		if (pSearchStatObj != null && pSearchStatObj.getTableName() != null) {
			String tableName = pSearchStatObj.getTableName();
			jdam.trace(
					sessionContext.getDam_Conn_Handle(),
					ip.UL_TM_MAJOR_EV,
					"Dynamic Schema for Statistics of table:<" + pSearchStatObj.getTableQualifier() + "."
							+ pSearchStatObj.getTableOwner() + "." + pSearchStatObj.getTableName() + "> is being requested\n");
	
			// FIXME
			// if (IsMatching???(pSearchStatObj, catalogName, schemaOwner, tableName)) {
			TableInfo tableInfo;
			tableInfo = sessionContext.getSchemaDescriptor().getTableInfo(tableName);
	
			addStatsInfoToDAM(tableInfo, pMemTree, iType, pList, pSearchObj, sessionContext);
		} else {
			jdam.trace(sessionContext.getDam_Conn_Handle(), ip.UL_TM_MAJOR_EV, "Dynamic Schema for all statistics of all tables is being requested\n");
	
			Set<String> tables;
			tables = sessionContext.getSchemaDescriptor().getTableNameSet();
			for (String tableName : tables) {
				TableInfo tableInfo;
				tableInfo = sessionContext.getSchemaDescriptor().getTableInfo(tableName);
				addStatsInfoToDAM(tableInfo, pMemTree, iType, pList, pSearchObj, sessionContext);
			}
		}
	}

	void addPrimaryKeyInfoToDAM(TableInfo tableInfo, long pMemTree, int iType, long pList, Object pSearchObj, SessionContext sessionContext) {
		String tableName = tableInfo.getName();
		schemaobj_pkey PkeyObj = new schemaobj_pkey();
		List<PkFkInfo> pkFkInfoList = tableInfo.getPkFks();
		String catalogName = (String) sessionContext.getProperty(RestIPConstants.CATALOG_NAME_PROP);
		String schemaOwner = (String) sessionContext.getProperty(RestIPConstants.SCHEMA_NAME_PROP);
	
		for (PkFkInfo pkFkInfo : pkFkInfoList) {
			String fkTableName = pkFkInfo.getFKTableName();
			String fkColumnName = pkFkInfo.getFKColumnName();
			String pkTableName = pkFkInfo.getPKTableName();
			String pkColumnName = pkFkInfo.getPKColumnName();
			String pkName = pkFkInfo.getPKName();
	
			if (pkColumnName == null || pkColumnName.length() == 0)
				continue;
	
			if (pkTableName != null && pkTableName.length() > 0 && pkTableName.equals(tableName) != true)
				continue;
	
			if (fkTableName != null && fkTableName.length() > 0 &&
					fkColumnName != null && fkColumnName.length() > 0)
				continue;
	
			if (pkName == null || pkName.length() == 0)
				pkName = RestIPConstants.PK_PREFIX + tableName + pkColumnName;
	
			PkeyObj.SetObjInfo(catalogName, schemaOwner, tableName, pkColumnName,
					pkFkInfo.getKeySeq(), pkName);
			jdam.dam_add_schemaobj(pMemTree,iType, pList, pSearchObj, PkeyObj);
		}
	}

	void addForeignKeyInfoToDAM(TableInfo tableInfo, long pMemTree, int iType, long pList, Object pSearchObj, SessionContext sessionContext) {
		String tableName = tableInfo.getName();
		schemaobj_fkey FkeyObj = new schemaobj_fkey();
		List<PkFkInfo> pkFkInfoList = tableInfo.getPkFks();
		String catalogName = (String) sessionContext.getProperty(RestIPConstants.CATALOG_NAME_PROP);
		String schemaOwner = (String) sessionContext.getProperty(RestIPConstants.SCHEMA_NAME_PROP);
	
		for (PkFkInfo pkFkInfo : pkFkInfoList) {
			String fkTableName = pkFkInfo.getFKTableName();
			String fkColumnName = pkFkInfo.getFKColumnName();
			String pkTableName = pkFkInfo.getPKTableName();
			String pkColumnName = pkFkInfo.getPKColumnName();
			String fkName = pkFkInfo.getFKName();
			String pkName = pkFkInfo.getPKName();
	
			if (fkColumnName == null || fkColumnName.length() == 0)
				continue;
	
			if (fkTableName != null && fkTableName.length() > 0 && fkTableName.equals(tableName) != true)
				continue;
	
			if (pkColumnName == null || pkColumnName.length() == 0)
				continue;
	
			if (pkTableName == null || pkColumnName.length() == 0)
				continue;
	
			if (fkName == null || fkName.length() == 0)
				fkName = RestIPConstants.FK_PREFIX + tableName + fkColumnName;
	
			if (pkName == null || pkName.length() == 0)
				pkName = RestIPConstants.PK_PREFIX + pkTableName + pkColumnName;
	
			FkeyObj.SetObjInfo(catalogName, schemaOwner, pkTableName, pkColumnName,
					catalogName, schemaOwner, tableName, fkColumnName,
					pkFkInfo.getKeySeq(), pkFkInfo.getUpdateRule(), pkFkInfo.getDeleteRule(),
					fkName, pkName);
			jdam.dam_add_schemaobj(pMemTree,iType, pList, pSearchObj, FkeyObj);
		}
	}

	void addStatsInfoToDAM(TableInfo tableInfo, long pMemTree, int iType, long pList, Object pSearchObj, SessionContext sessionContext) {
		schemaobj_stat StatObj = new schemaobj_stat();
		List<StatsInfo> stats = tableInfo.getStats();
		String catalogName = (String) sessionContext.getProperty(RestIPConstants.CATALOG_NAME_PROP);
		String schemaOwner = (String) sessionContext.getProperty(RestIPConstants.SCHEMA_NAME_PROP);

		if (null != stats) {
			for (StatsInfo statsInfo : stats) {
				String columnName = statsInfo.getColumnName();
				if (columnName != null && columnName.length() > 0) {
					String indexName = statsInfo.getIndexName();
					if (indexName == null || indexName.length() == 0)
						indexName = RestIPConstants.INDEX_PREFIX + tableInfo.getName() + columnName
								+ String.valueOf(statsInfo.getSeqInIndex());

					StatObj.SetObjInfo(catalogName, schemaOwner, tableInfo.getName(), statsInfo.getNonUnique(),
							statsInfo.getIndexQualifier(), indexName, statsInfo.getIndexType(), statsInfo.getSeqInIndex(),
							columnName, statsInfo.getCollation(), statsInfo.getCardinality(), statsInfo.getPages(),
							statsInfo.getFilterConditions());
					jdam.dam_add_schemaobj(pMemTree, iType, pList, pSearchObj, StatObj);
				}
			}
		}

	}

}
