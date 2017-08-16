/**
 * 
 */
package com.ddtek.common.schema;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author sjilani
 * 
 */
public class SchemaInfo {

	private Map<String, TableInfo> OATypeToTable = new LinkedHashMap<String, TableInfo>();

	public void addTableInfo(String name, TableInfo tableInfo) {
		OATypeToTable.put(name, tableInfo);
	}

	public TableInfo getTableInfo(String tableName) {
		TableInfo tableInfo = null;

		if (tableName != null) {
			String upperTableName = tableName.toUpperCase();
			tableInfo = OATypeToTable.get(upperTableName);
		}

		return tableInfo;
	}

	public Set<String> getTableNameSet() {
		return OATypeToTable.keySet();
	}

	public void initSchema(String schemaLocation)
			throws ParserConfigurationException, SAXException, IOException {

		initSchemaFromXML(schemaLocation);

		
	}
	
	public void initScheamFromJson(String schemaLocation){
		
		// code to parse from JSON file.
	}

	public void initSchemaFromXML(String schemaLocation)
			throws ParserConfigurationException, SAXException, IOException {

		File baseDir = new File(schemaLocation);

		for (File fileEntry : baseDir.listFiles()) {

			if (!fileEntry.isDirectory()) {

				DocumentBuilderFactory dbFactory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fileEntry.getAbsolutePath());

				doc.getDocumentElement().normalize();

				Element root = (Element) doc.getDocumentElement();

				NodeList nodeList = root.getElementsByTagName("table");

				for (int i = 0; i < nodeList.getLength(); i++) {

					Node node = (Node) nodeList.item(i);
					root = (Element) node;

					// Read attributes of table
					String tableName = root.getAttribute("name");
					String alias = root.getAttribute("alias");
					String userData = root.getAttribute("userData");

					TableInfo tableInfo = new TableInfo(tableName, alias,
							userData);

					tableInfo.setColumns(populateColumnInfo(root));
					tableInfo.setStats(populateStatsInfo(root));
					tableInfo.setPkFks(populatePkfkInfo(root));

					addTableInfo(tableInfo.getName().toUpperCase(), tableInfo);
				}
			}

		}
	}

	private Map<String,ColumnInfo> populateColumnInfo(Element root) {

		Map<String,ColumnInfo> columnList = new LinkedHashMap<String,ColumnInfo>();

		NodeList nodeList = root.getElementsByTagName("column");
		for (int i = 0; i < nodeList.getLength(); i++) {

			Node node = (Node) nodeList.item(i);
			root = (Element) node;

			String sName = root.getAttribute("name");
			String sFieldName = root.getAttribute("columnAlias");
			String uDataType = root.getAttribute("dataType");
			String lCharMaxLength = root.getAttribute("charMaxLength");
			String lNumericPrecision = root.getAttribute("numericPrecision");
			String sTypeName = root.getAttribute("typeName");
			String uNumericPrecisionRadix = root
					.getAttribute("numericPrecisionRadix");
			String uNumericScale = root.getAttribute("numbericScale");
			String uNullable = root.getAttribute("nullable");
			String uScope = root.getAttribute("scope");
			String sUserData = root.getAttribute("userData");
			String sOperatorSupport = root.getAttribute("operatorSupport");
			String uPseudoColumn = root.getAttribute("pseudoColumn");
			String uColumnType = root.getAttribute("columnType");
			String sRemarks = root.getAttribute("remarks");

			columnList.put(sName.toUpperCase(),new ColumnInfo(sName, sFieldName,
					getShort(uDataType), sTypeName, getInteger(lCharMaxLength),
					getInteger(lNumericPrecision),
					getShort(uNumericPrecisionRadix), getShort(uNumericScale),
					getShort(uNullable), getShort(uScope), sUserData,
					sOperatorSupport, getShort(uPseudoColumn),
					getShort(uColumnType), sRemarks));

		}

		return columnList;

	}

	private ArrayList<StatsInfo> populateStatsInfo(Element root) {
		ArrayList<StatsInfo> statList = new ArrayList<StatsInfo>();

		NodeList nodeList = root.getElementsByTagName("stat");
		for (int i = 0; i < nodeList.getLength(); i++) {

			Node node = (Node) nodeList.item(i);
			root = (Element) node;

			String uNonUnique = root.getAttribute("nonUnique");
			String sIndexQualifier = root.getAttribute("indexQualifier");
			String sIndexName = root.getAttribute("indexName");
			String uIndexType = root.getAttribute("indexType");
			String uSeqInIndex = root.getAttribute("seqInIndex");
			String sColumnName = root.getAttribute("columnName");
			String sCollation = root.getAttribute("collation");
			String lCardinality = root.getAttribute("cardinality");
			String lPages = root.getAttribute("pages");
			String sFilterConditions = root.getAttribute("filterConditions");

			statList.add(new StatsInfo(getShort(uNonUnique), sIndexQualifier,
					sIndexName, getShort(uIndexType), getShort(uSeqInIndex),
					sColumnName, sCollation, getInteger(lCardinality),
					getInteger(lPages), sFilterConditions));

		}

		return statList;
	}

	private ArrayList<PkFkInfo> populatePkfkInfo(Element root) {

		ArrayList<PkFkInfo> pkfkList = new ArrayList<PkFkInfo>();

		NodeList nodeList = root.getElementsByTagName("pkfk");
		for (int i = 0; i < nodeList.getLength(); i++) {

			Node node = (Node) nodeList.item(i);
			root = (Element) node;

			String sPKTableQualifier = root.getAttribute("pkTableQualifier");
			String sPKTableOwner = root.getAttribute("pkTableOwner");
			String sPKTableName = root.getAttribute("pkTableName");
			String sPKColumnName = root.getAttribute("pkColumnName");
			String sFKTableQualifier = root.getAttribute("pkTableQualifier");

			String sFKTableOwner = root.getAttribute("fkTableOwner");
			String sFKTableName = root.getAttribute("fkTableName");
			String sFKColumnName = root.getAttribute("fkColumnName");
			String uKeySeq = root.getAttribute("keySeq");
			String uUpdateRule = root.getAttribute("updateRule");

			String uDeleteRule = root.getAttribute("deleteRule");
			String sFKName = root.getAttribute("fkName");
			String sPKName = root.getAttribute("pkName");

			pkfkList.add(new PkFkInfo(sPKTableQualifier, sPKTableOwner,
					sPKTableName, sPKColumnName, sFKTableQualifier,
					sFKTableOwner, sFKTableName, sFKColumnName,
					getShort(uKeySeq), getShort(uUpdateRule),
					getShort(uDeleteRule), sFKName, sPKName));

		}

		return pkfkList;
	}

	public short getShort(String str) {

		short value = -1;

		if (null == str || str.trim().length() == 0) {
			value = -1;
		} else {
			value = Short.valueOf(str);
		}

		return value;
	}

	public int getInteger(String str) {

		int value = -1;

		if (null == str || str.trim().length() == 0) {
			value = -1;
		} else {
			value = Integer.valueOf(str);
		}

		return value;
	}

	public static void main(String args[]) throws ParserConfigurationException,
			SAXException, IOException {

		SchemaInfo schemaInfo = new SchemaInfo();
		schemaInfo
				.initSchema("C:\\ProgressWork\\CafoaWork\\CAFOA_7.2\\Connect\\Dev_Branches\\jilani\\CodeGen\\schema");

	}

}
