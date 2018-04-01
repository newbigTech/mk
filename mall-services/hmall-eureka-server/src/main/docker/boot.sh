if [ -n "$AGENT_ID" ]; then
    java $JAVA_OPTS -javaagent:pinpoint-agent-${PINGPOINT_VERSION}/pinpoint-bootstrap-${PINGPOINT_VERSION}.jar -Dpinpoint.agentId="${AGENT_ID}_${AGENT_NO}" -Dpinpoint.applicationName=${APPLICATION_NAME} -Djava.security.egd=file:/dev/./urandom -jar /app.jar
else
    java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar
fi
