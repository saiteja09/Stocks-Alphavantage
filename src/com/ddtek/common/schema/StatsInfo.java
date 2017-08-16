/**
 * 
 */
package com.ddtek.common.schema;



/**
 * @author sjilani
 *
 */
public class StatsInfo {

	private short	uNonUnique;
	private String	sIndexQualifier;
	private String	sIndexName;
	private short	uIndexType;
	private short	uSeqInIndex;
	private String	sColumnName;
	private String	sCollation;
	private int	lCardinality;
	private int	lPages;
	private String	sFilterConditions;

	/**
	 * @param uNonUnique
	 * @param sIndexQualifier
	 * @param sIndexName
	 * @param uIndexType
	 * @param uSeqInIndex
	 * @param sColumnName
	 * @param sCollation
	 * @param lCardinality
	 * @param lPages
	 * @param sFilterConditions
	 */
	public StatsInfo(short uNonUnique, String sIndexQualifier,
			String sIndexName, short uIndexType, short uSeqInIndex,
			String sColumnName, String sCollation, int lCardinality,
			int lPages, String sFilterConditions) {
		super();
		this.uNonUnique = uNonUnique;
		this.sIndexQualifier = sIndexQualifier == null? null : sIndexQualifier.toUpperCase();
		this.sIndexName = sIndexName == null? null : sIndexName.toUpperCase();
		this.uIndexType = uIndexType;
		this.uSeqInIndex = uSeqInIndex;
		this.sColumnName = sColumnName == null? null : sColumnName.toUpperCase();
		this.sCollation = sCollation;
		this.lCardinality = lCardinality;
		this.lPages = lPages;
		this.sFilterConditions = sFilterConditions;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.StatsInfo#setFilterConditions(java.lang.String)
	 */
	
	public StatsInfo() {
	}

	public void setFilterConditions(String sFilterConditions) {
		this.sFilterConditions = sFilterConditions;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.StatsInfo#getFilterConditions()
	 */
	
	public String getFilterConditions() {
		return sFilterConditions;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.StatsInfo#setPages(int)
	 */
	
	public void setPages(int lPages) {
		this.lPages = lPages;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.StatsInfo#getPages()
	 */
	
	public int getPages() {
		return lPages;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.StatsInfo#setCardinality(int)
	 */
	
	public void setCardinality(int lCardinality) {
		this.lCardinality = lCardinality;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.StatsInfo#getCardinality()
	 */
	
	public int getCardinality() {
		return lCardinality;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.StatsInfo#setCollation(java.lang.String)
	 */
	
	public void setCollation(String sCollation) {
		this.sCollation = sCollation;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.StatsInfo#getCollation()
	 */
	
	public String getCollation() {
		return sCollation;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.StatsInfo#setColumnName(java.lang.String)
	 */
	
	public void setColumnName(String sColumnName) {
		this.sColumnName = sColumnName == null? null : sColumnName.toUpperCase();
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.StatsInfo#getColumnName()
	 */
	
	public String getColumnName() {
		return sColumnName;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.StatsInfo#setSeqInIndex(short)
	 */
	
	public void setSeqInIndex(short uSeqInIndex) {
		this.uSeqInIndex = uSeqInIndex;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.StatsInfo#getSeqInIndex()
	 */
	
	public short getSeqInIndex() {
		return uSeqInIndex;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.StatsInfo#setType(short)
	 */
	
	public void setIndexType(short uIndexType) {
		this.uIndexType = uIndexType;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.StatsInfo#getType()
	 */
	
	public short getIndexType() {
		return uIndexType;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.StatsInfo#setIndexName(java.lang.String)
	 */
	
	public void setIndexName(String sIndexName) {
		this.sIndexName = sIndexName == null? null : sIndexName.toUpperCase();
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.StatsInfo#getIndexName()
	 */
	
	public String getIndexName() {
		return sIndexName;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.StatsInfo#setIndexQualifier(java.lang.String)
	 */
	
	public void setIndexQualifier(String sIndexQualifier) {
		this.sIndexQualifier = sIndexQualifier == null? null : sIndexQualifier.toUpperCase();
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.StatsInfo#getIndexQualifier()
	 */
	
	public String getIndexQualifier() {
		return sIndexQualifier;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.StatsInfo#setNonUnique(short)
	 */
	
	public void setNonUnique(short uNonUnique) {
		this.uNonUnique = uNonUnique;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.StatsInfo#getNonUnique()
	 */
	
	public short getNonUnique() {
		return uNonUnique;
	}

}
