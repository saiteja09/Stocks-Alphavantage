package com.ddtek.common.dataprocessor;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.ddtek.common.ip.SessionContext;
import com.ddtek.common.schema.ColumnInfo;
import oajava.sql.ip;
import oajava.sql.xo_tm;


/**
 * @author sjilani
 *
 */
public class DataConvertor {
	
	public String DEFAULT_TIMEZONE = "Etc/UTC";

	public DataConvertor() {
		getTsFormat().setTimeZone(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
	}

	public void initTimeZone(String timeZone) {
		if( timeZone != null) {
			timeZone = timeZone.trim();
			if (!timeZone.isEmpty() ){
				DEFAULT_TIMEZONE = timeZone;
			}
		}
	}

	public String ConvertValueForOA(String columnValue, short columnDataType, String columnUserData) throws Exception {

		if (columnValue == null)
			return columnValue;

		// Trim and check for empty or null,
		// the zero length strings will be treated as nulls.
		String colValue = columnValue.trim();
		if (colValue.isEmpty() || colValue.equalsIgnoreCase("NULL"))
			return null;

			switch(columnDataType) {
			case ip.XO_TYPE_BIT:
				if (columnValue.equalsIgnoreCase("true")) {
					columnValue = "1";
				} else {
					columnValue = "0";
				}

				break;
			case ip.XO_TYPE_DATE:
			case ip.XO_TYPE_DATE_TYPE:
			case ip.XO_TYPE_TIME:
			case ip.XO_TYPE_TIME_TYPE:
			case ip.XO_TYPE_TIMESTAMP:
			case ip.XO_TYPE_TIMESTAMP_TYPE:
				try {
					
					long colVal = Long.parseLong(columnValue);
					columnValue = getTsFormat().format(new Date(colVal));
				}
				catch (Exception e) {
					// Allows createdate field having Epoch_milli as well as YYYY-mm-dd strings 
				}
				break;
			}
		

		return columnValue;
	}

	public String ConvertValueForOA(String columnValue, ColumnInfo columnInfo) throws Exception {

		if (columnInfo == null)
			return columnValue;

		return ConvertValueForOA(columnValue, columnInfo.getDataType(), columnInfo.getUserdata());
	}

	public Object ConvertValueForService(Object columnValueObj, short columnDataType, String columnUserData) throws Exception {

			switch(columnDataType) {
			
			case ip.XO_TYPE_BIT:
				if (columnValueObj.toString().equalsIgnoreCase("1")) {
					columnValueObj = "true";
				} else {
					columnValueObj = "false";
				}

				break;
			case ip.XO_TYPE_DATE:
			case ip.XO_TYPE_DATE_TYPE:
			case ip.XO_TYPE_TIME:
			case ip.XO_TYPE_TIME_TYPE:
			case ip.XO_TYPE_TIMESTAMP:
			case ip.XO_TYPE_TIMESTAMP_TYPE:
				String colValue = null;
				String microSec = null;
				if (columnValueObj instanceof xo_tm) {
					xo_tm   xoTime = (xo_tm)columnValueObj;
					StringBuffer pSqlBuffer = new StringBuffer();

					pSqlBuffer.append(xoTime.getVal(xo_tm.YEAR)).append("-").append(xoTime.getVal(xo_tm.MONTH)+1).append("-").append(xoTime.getVal(xo_tm.DAY_OF_MONTH))
					.append(" ").append(xoTime.getVal(xo_tm.HOUR)).append(":").append(xoTime.getVal(xo_tm.MINUTE)).append(":").append(xoTime.getVal(xo_tm.SECOND))
					.append(".").append((int)(xoTime.getVal(xo_tm.FRACTION) * 0.000001));

					colValue = pSqlBuffer.toString();
					// microSec
					String fracSec = String.valueOf(xoTime.getVal(xo_tm.FRACTION));
					if (fracSec.length() == 9) {
						microSec = fracSec.substring(9-6, 9-3);
					}
				}
				else {
					colValue = columnValueObj.toString();
					microSec = "";
				}

				Long epoch = getTsFormat().parse(colValue).getTime();
				colValue = epoch.toString();
				columnValueObj = colValue;
				break;
			}
		

		return columnValueObj;
	}

	public Object ConvertValueForService(Object columnValueObj, ColumnInfo columnInfo) throws Exception {

		if (columnInfo == null)
			return columnValueObj;

		return ConvertValueForService(columnValueObj, columnInfo.getDataType(), columnInfo.getUserdata());
	}

	public SimpleDateFormat getTsFormat() {
		SimpleDateFormat tsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		tsFormat.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
		return tsFormat;
	}

}
