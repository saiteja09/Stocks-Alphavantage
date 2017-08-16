rem #============================================================================================
rem 	OpenAccess configuration tool for OpenAccess REST Code Generator product.
rem 	This tool removes OpenAccess JAVA service with the given service name. 
rem #============================================================================================



rem set username, if OpenAccess Agent is running as ServiceAdminAuthMethods=OSLogon(UID,PWD)

	set USERNAME=

rem set the name of the service to be deleted and the OpenAccess server location. 
rem set OA_DATASOURCE value and the service name will be set to %OA_DATASOURCE%Service.
	
	set OA_DATASOURCE=
	set SERVICENAME=%OA_DATASOURCE%Service
	set OASERVER=C:\Program Files\Progress\DataDirect\oaserver80
		
	
	echo "alc" > %SERVICENAME%.ini
	echo "%USERNAME%" >> %SERVICENAME%.ini	
	echo "sst" >> %SERVICENAME%.ini
	echo "%SERVICENAME%" >> %SERVICENAME%.ini
	echo "sd" >> %SERVICENAME%.ini
	echo "%SERVICENAME%" >> %SERVICENAME%.ini
	
	"%OASERVER%"\admin\oacla.exe < %SERVICENAME%.ini
	del %SERVICENAME%.ini

	echo Completed
