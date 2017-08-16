package com.ddtek.common.schema;

import java.util.HashMap;
import java.util.Map;

public class ColumnInfo  {

	static class ColumnTypeInfo {
		short		uDataType;
		String		sTypeName;
		int			lCharMaxLength;
		int			lNumericPrecision;
		short		uNumericPrecisionRadix;
		short		uNumericScale;
		short		uNullable;
		short		uScope;
		String		sUserData;
		String		sOperatorSupport;
		short		uPseudoColumn;
		short		uColumnType;
		String		sRemarks;

		public ColumnTypeInfo(short uDataType, String sTypeName,
				int lCharMaxLength, int lNumericPrecision,
				short uNumericPrecisionRadix, short uNumericScale,
				short uNullable, short uScope, String sUserData,
				String sOperatorSupport, short uPseudoColumn,
				short uColumnType, String sRemarks) {
			super();
			this.uDataType = uDataType;
			this.sTypeName = sTypeName;
			this.lCharMaxLength = lCharMaxLength;
			this.lNumericPrecision = lNumericPrecision;
			this.uNumericPrecisionRadix = uNumericPrecisionRadix;
			this.uNumericScale = uNumericScale;
			this.uNullable = uNullable;
			this.uScope = uScope;
			this.sUserData = sUserData;
			this.sOperatorSupport = sOperatorSupport;
			this.uPseudoColumn = uPseudoColumn;
			this.uColumnType = uColumnType;
			this.sRemarks = sRemarks;
		}
	}

	private static Map<Integer, ColumnTypeInfo> defaultColumnTypeInfo = new HashMap<Integer, ColumnInfo.ColumnTypeInfo>();
	static {
		defaultColumnTypeInfo.put(1,	new ColumnTypeInfo((short)1, "CHAR", 255, 255, (short)-1, (short)0, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(2,	new ColumnTypeInfo((short)2, "NUMERIC", 130, 127, (short)-1, (short)6, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(3,	new ColumnTypeInfo((short)3, "DECIMAL", 130, 127, (short)-1, (short)6, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(4,	new ColumnTypeInfo((short)4, "INTEGER", 4, 10, (short)-1, (short)0, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(5,	new ColumnTypeInfo((short)5, "SMALLINT", 2, 5, (short)-1, (short)0, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(6,	new ColumnTypeInfo((short)6, "FLOAT", 8, 15, (short)-1, (short)3, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(7,	new ColumnTypeInfo((short)7, "REAL", 4, 7, (short)-1, (short)3, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(8,	new ColumnTypeInfo((short)8, "DOUBLE", 8, 15, (short)-1, (short)3, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(9,	new ColumnTypeInfo((short)9, "DATE", 6, 10, (short)-1, (short)0, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(10,	new ColumnTypeInfo((short)10, "TIME", 6, 8, (short)-1, (short)0, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(11,	new ColumnTypeInfo((short)11, "TIMESTAMP", 16, 19, (short)-1, (short)0, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(12,	new ColumnTypeInfo((short)12, "VARCHAR", 8000, 8000, (short)-1, (short)0, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(91,	new ColumnTypeInfo((short)91, "DATE_TYPE", 6, 10, (short)-1, (short)0, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(92,	new ColumnTypeInfo((short)92, "TIME_TYPE", 6, 8, (short)-1, (short)0, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(93,	new ColumnTypeInfo((short)93, "TIMESTAMP_TYPE", 16, 19, (short)-1, (short)0, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(0,	new ColumnTypeInfo((short)0, "NULL", 0, 0, (short)-1, (short)0, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(-1,	new ColumnTypeInfo((short)-1, "LONGVARCHAR", 1000000, 1000000, (short)-1, (short)0, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(-2,	new ColumnTypeInfo((short)-2, "BINARY", 255, 255, (short)-1, (short)0, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(-3,	new ColumnTypeInfo((short)-3, "VARBINARY", 8000, 8000, (short)-1, (short)0, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(-4,	new ColumnTypeInfo((short)-4, "LONGVARBINARY", 1000000, 1000000, (short)-1, (short)0, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(-5,	new ColumnTypeInfo((short)-5, "BIGINT", 19, 19, (short)-1, (short)0, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(-6,	new ColumnTypeInfo((short)-6, "TINYINT", 1, 3, (short)-1, (short)0, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(-7,	new ColumnTypeInfo((short)-7, "BIT", 1, 1, (short)-1, (short)0, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(-8,	new ColumnTypeInfo((short)-8, "WCHAR", 510, 255, (short)-1, (short)0, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(-9,	new ColumnTypeInfo((short)-9, "WVARCHAR", 16000, 8000, (short)-1, (short)0, (short)1, (short)-1, null, null, (short)1, (short)0, null));
		defaultColumnTypeInfo.put(-10,	new ColumnTypeInfo((short)-10, "WLONGVARCHAR", 2000000, 1000000, (short)-1, (short)0, (short)1, (short)-1, null, null, (short)1, (short)0, null));
	}

	private String		sName;
	private String		sNameForFieldName;
	private String		sFieldName;
	private short		uDataType;
	private String		sTypeName;
	private int			lCharMaxLength;
	private int			lNumericPrecision;
	private short		uNumericPrecisionRadix;
	private short		uNumericScale;
	private short		uNullable;
	private short		uScope;
	private String		sUserData;
	private String		sOperatorSupport;
	private short		uPseudoColumn;
	private short		uColumnType;
	private String		sRemarks;

	/**
	 * @param sFieldName
	 * @param uDataType
	 * @param sUserData
	 */
	public ColumnInfo(String sFieldName, short uDataType, String sUserData) {
		this(sFieldName, sFieldName, uDataType, null, -1, -1,
				(short)-1, (short)-1, (short)-1, (short)-1,
				sUserData, null, (short)-1, (short)-1, null);
	}

	/**
	 * @param sName
	 * @param sFieldName
	 * @param uDataType
	 * @param sTypeName
	 * @param lCharMaxLength
	 * @param lNumericPrecision
	 * @param uNumericPrecisionRadix
	 * @param uNumericScale
	 * @param uNullable
	 * @param uScope
	 * @param sUserData
	 * @param sOperatorSupport
	 * @param uPseudoColumn
	 * @param uColumnType
	 * @param sRemarks
	 */
	public ColumnInfo(String sName, String sFieldName, short uDataType, String sTypeName,
			int lCharMaxLength, int lNumericPrecision,
			short uNumericPrecisionRadix, short uNumericScale, short uNullable,
			short uScope, String sUserData, String sOperatorSupport,
			short uPseudoColumn, short uColumnType, String sRemarks) {
		super();

		ColumnTypeInfo defaultInfo = defaultColumnTypeInfo.get(Integer.valueOf(uDataType));
		if (defaultInfo == null) {
			// Use VARCHAR as fall-back type
			defaultInfo = defaultColumnTypeInfo.get(12);
		}

		this.sName = sName == null? null : sName.toUpperCase().replaceAll("\\+|-|\\*|:|;|/|%|\\|| ", "_");
		this.sNameForFieldName = sName;
		this.sFieldName = sFieldName;
		this.uDataType = defaultInfo.uDataType;
		this.sTypeName = (sTypeName == null || sTypeName.length() <= 0) ? defaultInfo.sTypeName : sTypeName.toUpperCase();
		this.lCharMaxLength = lCharMaxLength < 0 ? defaultInfo.lCharMaxLength : lCharMaxLength;
		this.lNumericPrecision = lNumericPrecision < 0 ? defaultInfo.lNumericPrecision : lNumericPrecision;
		this.uNumericPrecisionRadix = uNumericPrecisionRadix < 0 ? defaultInfo.uNumericPrecisionRadix : uNumericPrecisionRadix;
		this.uNumericScale = uNumericScale < 0 ? defaultInfo.uNumericScale : uNumericScale;
		this.uNullable = uNullable < 0 ? defaultInfo.uNullable : uNullable;
		this.uScope = uScope < 0 ? defaultInfo.uScope : uScope;
		this.sUserData = (sUserData == null || sUserData.length() <= 0) ? defaultInfo.sUserData : sUserData;
		this.sOperatorSupport = sOperatorSupport;
		this.uPseudoColumn = uPseudoColumn <= 0 ? defaultInfo.uPseudoColumn : uPseudoColumn;
		this.uColumnType = uColumnType <= 0 ? defaultInfo.uColumnType : uColumnType;
		this.sRemarks = (sRemarks == null || sRemarks.length() <= 0) ? defaultInfo.sRemarks : sRemarks;
	}
	
	
	




	public String getName() {
		return sName;
	}

	/* (non-Javadoc)
	 * @see oajava.ws.ColumnInfo#setName(java.lang.String)
	 */
	
	public void setName(String name) {
		this.sName = name == null? null : name.toUpperCase();
		this.sNameForFieldName = name;
	}

	
	public short getDataType() {
		return uDataType;
	}

	
	public void setDataType(short dataType) {
		uDataType = dataType;
	}

	
	public String getTypeName() {
		return sTypeName;
	}

	
	public void setTypeName(String typeName) {
		sTypeName = typeName == null? null : typeName.toUpperCase();
	}

	
	public int getCharMaxlength() {
		return lCharMaxLength;
	}

	
	public void setCharMaxlength(int charMaxLenght) {
		lCharMaxLength = charMaxLenght;
	}

	
	public int getNumericPrecision() {
		return lNumericPrecision;
	}

	
	public void setNumericPrecision(int numericPrecision) {
		lNumericPrecision = numericPrecision;
	}

	
	public short getNumericPrecisionRadix() {
		return uNumericPrecisionRadix;
	}

	
	public void setNumericPrecisionRadix(short numericPrecisionRadix) {
		uNumericPrecisionRadix = numericPrecisionRadix;
	}

	
	public short getNumericScale() {
		return uNumericScale;
	}

	
	public void setNumericScale(short numericScale) {
		uNumericScale = numericScale;
	}

	
	public short getNullable() {
		return uNullable;
	}

	
	public void setNullable(short nullable) {
		uNullable = nullable;
	}

	
	public short getScope() {
		return uScope;
	}

	
	public void setScope(short scope) {
		uScope = scope;
	}

	
	public String getOperatorSupport() {
		return sOperatorSupport;
	}

	
	public void setOperatorSupport(String operatorSupport) {
		sOperatorSupport = operatorSupport;
	}

	
	public String getUserdata() {
		return sUserData;
	}

	
	public void setUserdata(String userdata) {
		sUserData = userdata;
	}

	
	public short getPseudoColumn() {
		return uPseudoColumn;
	}

	
	public void setPseudoColumn(short pseudoColumn) {
		uPseudoColumn = pseudoColumn;
	}

	
	public short getColumnType() {
		return uColumnType;
	}

	
	public void setColumnType(short columnType) {
		uColumnType = columnType;
	}

	
	public String getRemarks() {
		return sRemarks;
	}

	
	public void setRemarks(String remarks) {
		sRemarks = remarks;
	}

	
	public String getFieldName() {
		return (sFieldName == null || sFieldName.length() ==0) ? sNameForFieldName : sFieldName;
	}

	
	public void setFieldName(String fieldName) {
		sFieldName = fieldName;
	}

}
