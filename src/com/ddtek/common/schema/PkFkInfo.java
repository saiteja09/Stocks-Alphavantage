/**
 * 
 */
package com.ddtek.common.schema;


/**
 * @author sjilani
 *
 */
public class PkFkInfo {

	private String	sPKTableQualifier;
	private String	sPKTableOwner;
	private String	sPKTableName;
	private String	sPKColumnName;
	private String	sFKTableQualifier;
	private String	sFKTableOwner;
	private String	sFKTableName;
	private String	sFKColumnName;
	private short	uKeySeq;
	private short	uUpdateRule;
	private short	uDeleteRule;
	private String	sFKName;
	private String	sPKName;

	/**
	 * @param sPKTableQualifier
	 * @param sPKTableOwner
	 * @param sPKTableName
	 * @param sPKColumnName
	 * @param sFKTableQualifier
	 * @param sFKTableOwner
	 * @param sFKTableName
	 * @param sFKColumnName
	 * @param uKeySeq
	 * @param uUpdateRule
	 * @param uDeleteRule
	 * @param sFKName
	 * @param sPKName
	 */
	public PkFkInfo(String sPKTableQualifier, String sPKTableOwner,
			String sPKTableName, String sPKColumnName,
			String sFKTableQualifier, String sFKTableOwner,
			String sFKTableName, String sFKColumnName, short uKeySeq,
			short uUpdateRule, short uDeleteRule, String sFKName, String sPKName) {
		super();
		this.sPKTableQualifier = sPKTableQualifier == null? null : sPKTableQualifier.toUpperCase();
		this.sPKTableOwner = sPKTableOwner == null? null : sPKTableOwner.toUpperCase();
		this.sPKTableName = sPKTableName == null? null : sPKTableName.toUpperCase();
		this.sPKColumnName = sPKColumnName == null? null : sPKColumnName.toUpperCase();
		this.sFKTableQualifier = sFKTableQualifier == null? null : sFKTableQualifier.toUpperCase();
		this.sFKTableOwner = sFKTableOwner == null? null : sFKTableOwner.toUpperCase();
		this.sFKTableName = sFKTableName == null? null : sFKTableName.toUpperCase();
		this.sFKColumnName = sFKColumnName == null? null : sFKColumnName.toUpperCase();
		this.uKeySeq = uKeySeq;
		this.uUpdateRule = uUpdateRule;
		this.uDeleteRule = uDeleteRule;
		this.sFKName = sFKName == null? null : sFKName.toUpperCase();
		this.sPKName = sPKName == null? null : sPKName.toUpperCase();
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.PkFkInfo#setPKName(java.lang.String)
	 */
	
	public void setPKName(String sPKName) {
		this.sPKName = sPKName == null? null : sPKName.toUpperCase();
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.PkFkInfo#getPKName()
	 */
	
	public String getPKName() {
		return sPKName;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.PkFkInfo#setFKName(java.lang.String)
	 */
	
	public void setFKName(String sFKName) {
		this.sFKName = sFKName == null? null : sFKName.toUpperCase();
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.PkFkInfo#getFKName()
	 */
	
	public String getFKName() {
		return sFKName;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.PkFkInfo#setDeleteRule(short)
	 */
	
	public void setDeleteRule(short uDeleteRule) {
		this.uDeleteRule = uDeleteRule;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.PkFkInfo#getDeleteRule()
	 */
	
	public short getDeleteRule() {
		return uDeleteRule;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.PkFkInfo#setUpdateRule(short)
	 */
	
	public void setUpdateRule(short uUpdateRule) {
		this.uUpdateRule = uUpdateRule;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.PkFkInfo#getUpdateRule()
	 */
	
	public short getUpdateRule() {
		return uUpdateRule;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.PkFkInfo#setKeySeq(short)
	 */
	
	public void setKeySeq(short uKeySeq) {
		this.uKeySeq = uKeySeq;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.PkFkInfo#getKeySeq()
	 */
	
	public short getKeySeq() {
		return uKeySeq;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.PkFkInfo#setFKColumnName(java.lang.String)
	 */
	
	public void setFKColumnName(String sFKColumnName) {
		this.sFKColumnName = sFKColumnName == null? null : sFKColumnName.toUpperCase();
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.PkFkInfo#getFKColumnName()
	 */
	
	public String getFKColumnName() {
		return sFKColumnName;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.PkFkInfo#setFKTableName(java.lang.String)
	 */
	
	public void setFKTableName(String sFKTableName) {
		this.sFKTableName = sFKTableName == null? null : sFKTableName.toUpperCase();
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.PkFkInfo#getFKTableName()
	 */
	
	public String getFKTableName() {
		return sFKTableName;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.PkFkInfo#setFKTableOwner(java.lang.String)
	 */
	
	public void setFKTableOwner(String sFKTableOwner) {
		this.sFKTableOwner = sFKTableOwner == null? null : sFKTableOwner.toUpperCase();
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.PkFkInfo#getFKTableOwner()
	 */
	
	public String getFKTableOwner() {
		return sFKTableOwner;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.PkFkInfo#setFKTableQualifier(java.lang.String)
	 */
	
	public void setFKTableQualifier(String sFKTableQualifier) {
		this.sFKTableQualifier = sFKTableQualifier == null? null : sFKTableQualifier.toUpperCase();
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.PkFkInfo#getFKTableQualifier()
	 */
	
	public String getFKTableQualifier() {
		return sFKTableQualifier;
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.PkFkInfo#setPKColumnName(java.lang.String)
	 */
	
	public void setPKColumnName(String sPKColumnName) {
		this.sPKColumnName = sPKColumnName == null? null : sPKColumnName.toUpperCase();
	}

	/* (non-Javadoc)
	 * @see oajava.genws.service.PkFkInfo#getPKColumnName()
	 */
	
	public String getPKColumnName() {
		return sPKColumnName;
	}

	
	public void setPKTableName(String sPKTableName) {
		this.sPKTableName = sPKTableName == null? null : sPKTableName.toUpperCase();
	}

	
	public String getPKTableName() {
		return sPKTableName;
	}

	
	public void setPKTableOwner(String sPKTableOwner) {
		this.sPKTableOwner = sPKTableOwner == null? null : sPKTableOwner.toUpperCase();
	}

	
	public String getPKTableOwner() {
		return sPKTableOwner;
	}

	
	public void setPKTableQualifier(String sPKTableQualifier) {
		this.sPKTableQualifier = sPKTableQualifier == null? null : sPKTableQualifier.toUpperCase();
	}

	
	public String getPKTableQualifier() {
		return sPKTableQualifier;
	}

}
