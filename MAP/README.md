mk项目的中台系统

# hmallParent

## 运行配置说明
1. 在本地开启redis-server。使用默认配置启动redis即可。
2. tomcat 需要配置 context.xml：
`<Resource auth="Container" driverClassName="oracle.jdbc.driver.OracleDriver" name="jdbc/hap_dev" type="javax.sql.DataSource" url="jdbc:oracle:thin:@10.5.30.101:1521:mkoracle" username="hap_dev" password="hap_dev"/>`
3. 双击tomcat进入tomcat配置界面，将Timeouts start in(启动时间)从默认的45秒提高到120秒。
4. 将core在server中运行。