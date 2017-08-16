#!/bin/bash
##============================================================================================
# 	OpenAccess configuration tool for OpenAccess REST Code Generator product.
# 	This tool removes OpenAccess JAVA service with the given service name. 
##============================================================================================


	OASERVER=/users/oaserver80
	

#Remove Service 

# SET OA_DATASOURCE value. This will remove the service with name ${OA_DATASOURCE}Service.

	OA_DATASOURCE=
	SERVICENAME=${OA_DATASOURCE}Service
	
	echo "Removing Service " ${SERVICENAME}

	echo "alc" > ${SERVICENAME}.cfg
    
	echo "sst" >> ${SERVICENAME}.cfg

	echo "${SERVICENAME}" >> ${SERVICENAME}.cfg

	echo "sd" >> ${SERVICENAME}.cfg

	echo "${SERVICENAME}" >> ${SERVICENAME}.cfg

	
	"${OASERVER}"/admin/oacla.sh < ${SERVICENAME}.cfg

	rm ${SERVICENAME}.cfg

	echo "Completed"
