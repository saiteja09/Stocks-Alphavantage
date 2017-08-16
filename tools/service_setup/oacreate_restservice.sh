#!/bin/bash
#============================================================================================
# OpenAccess configuration tool for OpenAccess REST Code Generator product.
# This tool creates OpenAccess JAVA service with the given service name and a data source.
#============================================================================================

clear
	OASERVER=/usr/oaserver80
	SERVICE_PORT=19993
	OA_DATASOURCE=Sample
	DATASOURCEIPCLASS=com/ddtek/common/ip/RestIP
	RESOURCE_PROPERTIES=${OASERVER}/ip/RestGenerator/schema
# If you want to use CustomProperties, set them using DataSourceIPCustomProperties property.
# In this script, for sample, Property1=? is used. The value of this Property1 can be given by 
# user by using connection url during connection.
	

	USERNAME=$1
	PASSWORD=$2
	
    echo "alc" > ${OA_DATASOURCE}.cfg
    echo "sc" >> ${OA_DATASOURCE}.cfg
    echo "${OA_DATASOURCE}Service" >> ${OA_DATASOURCE}.cfg
    echo "OA80_OpenAccessSDKforJava" >> ${OA_DATASOURCE}.cfg
    echo ${SERVICE_PORT} >> ${OA_DATASOURCE}.cfg
    
# ServiceIPModule
	echo "saa" >> ${OA_DATASOURCE}.cfg
    echo "${OA_DATASOURCE}Service" >> ${OA_DATASOURCE}.cfg
    echo "ServiceIPModule" >> ${OA_DATASOURCE}.cfg
    echo "oadamipjava.so" >> ${OA_DATASOURCE}.cfg
	

# ServiceJVMClassPath
    echo "saa" >> ${OA_DATASOURCE}.cfg
    echo "${OA_DATASOURCE}Service" >> ${OA_DATASOURCE}.cfg
    echo "ServiceJVMClassPath" >> ${OA_DATASOURCE}.cfg
    echo "${OASERVER}/ip/oajava/oasql.jar:${OASERVER}/ip/RestGenerator/${OA_DATASOURCE}/${OA_DATASOURCE}.jar" >> ${OA_DATASOURCE}.cfg

		
# ServiceSQLDiskCacheMaxSize
    echo "saa" >> ${OA_DATASOURCE}.cfg
    echo "${OA_DATASOURCE}Service" >> ${OA_DATASOURCE}.cfg
    echo "ServiceSQLDiskCacheMaxSize" >> ${OA_DATASOURCE}.cfg
    echo "4096" >> ${OA_DATASOURCE}.cfg
	
	
# ServiceDebugLogPath
    echo "sar" >> ${OA_DATASOURCE}.cfg
    echo "${OA_DATASOURCE}Service" >> ${OA_DATASOURCE}.cfg
    echo "ServiceDebugLogPath" >> ${OA_DATASOURCE}.cfg
    echo "${OASERVER}/logging/${OA_DATASOURCE}" >> ${OA_DATASOURCE}.cfg
	
	
# ServiceSQLEngineMessagePrefix
	echo "saa" >> ${OA_DATASOURCE}.cfg
    echo "${OA_DATASOURCE}Service" >> ${OA_DATASOURCE}.cfg
    echo "ServiceSQLEngineMessagePrefix" >> ${OA_DATASOURCE}.cfg
    echo "[${OA_DATASOURCE}]" >> ${OA_DATASOURCE}.cfg    	
    
	
# ServiceSQLDiskCachePath
    echo "sar" >> ${OA_DATASOURCE}.cfg
    echo "${OA_DATASOURCE}Service" >> ${OA_DATASOURCE}.cfg
    echo "ServiceSQLDiskCachePath" >> ${OA_DATASOURCE}.cfg
    echo "${OASERVER}/ip/cache/${OA_DATASOURCE}" >> ${OA_DATASOURCE}.cfg
	
	
# Create data source	
    echo "dsc" >> ${OA_DATASOURCE}.cfg
    echo "${OA_DATASOURCE}Service" >> ${OA_DATASOURCE}.cfg
    echo "${OA_DATASOURCE}" >> ${OA_DATASOURCE}.cfg
    
	
# DataSourceIPType
	echo "dsar" >> ${OA_DATASOURCE}.cfg
    echo "${OA_DATASOURCE}Service" >> ${OA_DATASOURCE}.cfg
    echo "${OA_DATASOURCE}" >> ${OA_DATASOURCE}.cfg
    echo "DataSourceIPType" >> ${OA_DATASOURCE}.cfg
    echo "DAMIP" >> ${OA_DATASOURCE}.cfg
    
	
# DataSourceIPSchemaPath
	echo "dsar" >> ${OA_DATASOURCE}.cfg
    echo "${OA_DATASOURCE}Service" >> ${OA_DATASOURCE}.cfg
    echo "${OA_DATASOURCE}" >> ${OA_DATASOURCE}.cfg
    echo "DataSourceIPSchemaPath" >> ${OA_DATASOURCE}.cfg
    echo "${OASERVER}/ip/RestGenerator/${OA_DATASOURCE}/schema" >> ${OA_DATASOURCE}.cfg
    
	
# DataSourceLogonMethod
	echo "dsar" >> ${OA_DATASOURCE}.cfg
    echo "${OA_DATASOURCE}Service" >> ${OA_DATASOURCE}.cfg
    echo "${OA_DATASOURCE}" >> ${OA_DATASOURCE}.cfg
    echo "DataSourceLogonMethod" >> ${OA_DATASOURCE}.cfg
    echo "Anonymous" >> ${OA_DATASOURCE}.cfg
    
	
# DataSourceIPClass
	echo "dsaa" >> ${OA_DATASOURCE}.cfg
    echo "${OA_DATASOURCE}Service" >> ${OA_DATASOURCE}.cfg
    echo "${OA_DATASOURCE}" >> ${OA_DATASOURCE}.cfg
    echo "DataSourceIPClass" >> ${OA_DATASOURCE}.cfg
    echo "${DATASOURCEIPCLASS}" >> ${OA_DATASOURCE}.cfg
	
	
# DataSourceIPProperties
    echo "dsaa" >> ${OA_DATASOURCE}.cfg
    echo "${OA_DATASOURCE}Service" >> ${OA_DATASOURCE}.cfg
    echo "${OA_DATASOURCE}" >> ${OA_DATASOURCE}.cfg
    echo "DataSourceIPProperties" >> ${OA_DATASOURCE}.cfg
    echo "Resource_Properties=${RESOURCE_PROPERTIES}" >> ${OA_DATASOURCE}.cfg
	
	
# DataSourceIPCustomProperties
    echo "dsaa" >> ${OA_DATASOURCE}.cfg
    echo "${OA_DATASOURCE}Service" >> ${OA_DATASOURCE}.cfg	
    echo "${OA_DATASOURCE}" >> ${OA_DATASOURCE}.cfg
    echo "DataSourceIPCustomProperties" >> ${OA_DATASOURCE}.cfg
    echo "Property1=?" >> ${OA_DATASOURCE}.cfg
	


	${OASERVER}/admin/oacla.sh -uid $USERNAME -pwd $PASSWORD < ${OA_DATASOURCE}.cfg
	
	mkdir "${OASERVER}/logging/${OA_DATASOURCE}"
	mkdir "${OASERVER}/ip/cache/${OA_DATASOURCE}"
	rm -f -r ${OA_DATASOURCE}.cfg
