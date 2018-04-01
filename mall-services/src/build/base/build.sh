#!/bin/sh
fileName=(order user product )
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