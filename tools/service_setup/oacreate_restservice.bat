rem #============================================================================================
rem 	OpenAccess configuration tool for OpenAccess REST Code Generator product.
rem This tool creates OpenAccess JAVA service with the given service name and a data source. 
rem #============================================================================================



rem set username, if OpenAccess Agent is running as ServiceAdminAuthMethods=OSLogon(UID,PWD)
	set USERNAME=
	set OASERVER=C:\Program Files\Progress\DataDirect\oaserver80
	set JVMLOCATION=C:\Program Files\Java\jdk1.6.0_32\jre\bin\server
	set SERVICE_PORT=19993
	set OA_DATASOURCE=Sample
	set DATASOURCEIPCLASS=com/ddtek/common/ip/RestIP
	set RESOURCE_PROPERTIES=%OASERVER%/ip/RestGenerator/schema
	
	

rem If you want to use CustomProperties, set them using DataSourceIPCustomProperties property.
rem In this script, for sample, Property1=? is used. The value of this Property1 can be given by 
rem user by using connection url during connection.
	
	
	
    echo "alc" > %OA_DATASOURCE%.ini
    echo "%USERNAME%" >> %OA_DATASOURCE%.ini
    echo "sc" >> %OA_DATASOURCE%.ini
    echo "%OA_DATASOURCE%Service" >> %OA_DATASOURCE%.ini
    echo "OA80_OpenAccessSDKforJava" >> %OA_DATASOURCE%.ini
    echo %SERVICE_PORT% >> %OA_DATASOURCE%.ini
    
rem ServiceIPModule
	echo "saa" >> %OA_DATASOURCE%.ini
    echo "%OA_DATASOURCE%Service" >> %OA_DATASOURCE%.ini
    echo "ServiceIPModule" >> %OA_DATASOURCE%.ini
    echo "oadamipjava.dll" >> %OA_DATASOURCE%.ini
	

rem ServiceJVMClassPath
    echo "saa" >> %OA_DATASOURCE%.ini
    echo "%OA_DATASOURCE%Service" >> %OA_DATASOURCE%.ini
    echo "ServiceJVMClassPath" >> %OA_DATASOURCE%.ini
    echo "%OASERVER%\ip\oajava\oasql.jar;%OASERVER%\ip\RestGenerator\%OA_DATASOURCE%\%OA_DATASOURCE%.jar" >> %OA_DATASOURCE%.ini
	
rem ServiceJVMLocation
    echo "saa" >> %OA_DATASOURCE%.ini
    echo "%OA_DATASOURCE%Service" >> %OA_DATASOURCE%.ini
    echo "ServiceJVMLocation" >> %OA_DATASOURCE%.ini
    echo "%JVMLOCATION%" >> %OA_DATASOURCE%.ini
			
		
rem ServiceSQLDiskCacheMaxSize
    echo "saa" >> %OA_DATASOURCE%.ini
    echo "%OA_DATASOURCE%Service" >> %OA_DATASOURCE%.ini
    echo "ServiceSQLDiskCacheMaxSize" >> %OA_DATASOURCE%.ini
    echo "4096" >> %OA_DATASOURCE%.ini
	
rem ServiceDebugLogPath
    echo "sar" >> %OA_DATASOURCE%.ini
    echo "%OA_DATASOURCE%Service" >> %OA_DATASOURCE%.ini
    echo "ServiceDebugLogPath" >> %OA_DATASOURCE%.ini
    echo "%OASERVER%\logging\%OA_DATASOURCE%" >> %OA_DATASOURCE%.ini
	
rem ServiceSQLEngineMessagePrefix
	echo "saa" >> %OA_DATASOURCE%.ini
    echo "%OA_DATASOURCE%Service" >> %OA_DATASOURCE%.ini
    echo "ServiceSQLEngineMessagePrefix" >> %OA_DATASOURCE%.ini
    echo "[%OA_DATASOURCE%]" >> %OA_DATASOURCE%.ini    	
    
rem ServiceSQLDiskCachePath
    echo "sar" >> %OA_DATASOURCE%.ini
    echo "%OA_DATASOURCE%Service" >> %OA_DATASOURCE%.ini
    echo "ServiceSQLDiskCachePath" >> %OA_DATASOURCE%.ini
    echo "%OASERVER%\ip\cache\%OA_DATASOURCE%" >> %OA_DATASOURCE%.ini
	
	
    echo "dsc" >> %OA_DATASOURCE%.ini
    echo "%OA_DATASOURCE%Service" >> %OA_DATASOURCE%.ini
    echo "%OA_DATASOURCE%" >> %OA_DATASOURCE%.ini
    
rem DataSourceIPType
	echo "dsar" >> %OA_DATASOURCE%.ini
    echo "%OA_DATASOURCE%Service" >> %OA_DATASOURCE%.ini
    echo "%OA_DATASOURCE%" >> %OA_DATASOURCE%.ini
    echo "DataSourceIPType" >> %OA_DATASOURCE%.ini
    echo "DAMIP" >> %OA_DATASOURCE%.ini
    
rem DataSourceIPSchemaPath
	echo "dsar" >> %OA_DATASOURCE%.ini
    echo "%OA_DATASOURCE%Service" >> %OA_DATASOURCE%.ini
    echo "%OA_DATASOURCE%" >> %OA_DATASOURCE%.ini
    echo "DataSourceIPSchemaPath" >> %OA_DATASOURCE%.ini
    echo "%OASERVER%\ip\RestGenerator\%OA_DATASOURCE%\schema" >> %OA_DATASOURCE%.ini
    
rem DataSourceLogonMethod
	echo "dsar" >> %OA_DATASOURCE%.ini
    echo "%OA_DATASOURCE%Service" >> %OA_DATASOURCE%.ini
    echo "%OA_DATASOURCE%" >> %OA_DATASOURCE%.ini
    echo "DataSourceLogonMethod" >> %OA_DATASOURCE%.ini
    echo "Anonymous" >> %OA_DATASOURCE%.ini
    
rem DataSourceIPClass
	echo "dsaa" >> %OA_DATASOURCE%.ini
    echo "%OA_DATASOURCE%Service" >> %OA_DATASOURCE%.ini
    echo "%OA_DATASOURCE%" >> %OA_DATASOURCE%.ini
    echo "DataSourceIPClass" >> %OA_DATASOURCE%.ini
    echo "%DATASOURCEIPCLASS%" >> %OA_DATASOURCE%.ini
	
rem DataSourceIPProperties
    echo "dsaa" >> %OA_DATASOURCE%.ini
    echo "%OA_DATASOURCE%Service" >> %OA_DATASOURCE%.ini
    echo "%OA_DATASOURCE%" >> %OA_DATASOURCE%.ini
    echo "DataSourceIPProperties" >> %OA_DATASOURCE%.ini
    echo "Resource_Properties=%RESOURCE_PROPERTIES%" >> %OA_DATASOURCE%.ini
	
rem DataSourceIPCustomProperties
    echo "dsaa" >> %OA_DATASOURCE%.ini
    echo "%OA_DATASOURCE%Service" >> %OA_DATASOURCE%.ini	
    echo "%OA_DATASOURCE%" >> %OA_DATASOURCE%.ini
    echo "DataSourceIPCustomProperties" >> %OA_DATASOURCE%.ini
    echo "Property1=?" >> %OA_DATASOURCE%.ini
	


	"%OASERVER%"\admin\oacla.exe < %OA_DATASOURCE%.ini
	mkdir "%OASERVER%\logging\%OA_DATASOURCE%"
	mkdir "%OASERVER%\ip\cache\%OA_DATASOURCE%"
	del %OA_DATASOURCE%.ini
	
