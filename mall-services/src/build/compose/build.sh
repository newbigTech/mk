#!/bin/sh
fileName=( hap-service  hmall-after-sale hmall-drools-service hmall-od-service hmall-pd-service hmall-promote-server hmall-ur-service hmall-ps-service hmall-aftersale-service hmall-logistics-service hpay-api-gateway  hpay-thirdparty-service hpay-back-service )
for file in ${fileName[*]}
do
    if test -d ${file}
    then
        echo "build :" ${file}".jar"
        cd ${file}
        mvn clean package -DskipTests=true -U
        echo "build : "${file}".jar :success"
        cd ..
    fi
done