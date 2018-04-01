FROM registry.saas.hand-china.com/tools/pinpoint-agent
ADD hmall-api-gateway.jar app.jar
ADD boot.sh boot.sh
RUN sh -c 'chmod +x /boot.sh'
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "/boot.sh" ]