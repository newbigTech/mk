#!/bin/sh
fileName=(hmall-eureka-server hmall-api-gateway )
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