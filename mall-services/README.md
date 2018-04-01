mk的促销微服务模块

<<<<<<< HEAD
微服务::

api-gateway 引用本地jar包（external-libs/log-agent-1.0-SNAPSHOT.jar），需要手动导入maven库:

mvn install:install-file -Dfile=log-agent-1.0-SNAPSHOT.jar -DgroupId=com.markor.unilog -DartifactId=log-agent -Dversion=1.0-SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=qqConnect-20130107.jar -DgroupId=com.qq.connect -DartifactId=qqConnect -Dversion=20130107 -Dpackaging=jar
mvn install:install-file -Dfile=log-agent-1.0-SNAPSHOT.jar -DgroupId=com.markor.unilog -DartifactId=log-agent -Dversion=1.0-SNAPSHOT -Dpackaging=jar
mvn install:install-file  -Dfile=util-1.0-20170512.013145-126.jar -DgroupId=com.hand.hmall -DartifactId=mk-util -Dversion=1.0-20170512.013145-126 -Dpackaging=jar
=======
  微服务

  build前须导入本地jar包：
  文件位置：external-libs/log-agent-1.0-SNAPSHOT.jar
  执行命令：
  mvn install:install-file -Dfile=log-agent-1.0-SNAPSHOT.jar -DgroupId=com.markor.unilog -DartifactId=log-agent -Dversion=1.0-SNAPSHOT -Dpackaging=jar

  Jenkins服务器须将安装后的maven库copy至Jenkins的maven仓库中：

  mkdir -p /var/lib/jenkins/.m2/repository/com/markor/unilog/log-agent

  cp -R /root/.m2/repository/com/markor/unilog/log-agent/* /var/lib/jenkins/.m2/repository/com/markor/unilog/log-agent
>>>>>>> gateway

  mkdir -p /var/lib/jenkins/.m2/repository/com/hand/hmall/mk-util
  cp -R /root/.m2/repository/com/hand/hmall/mk-util/* /var/lib/jenkins/.m2/repository/com/hand/hmall/mk-util
