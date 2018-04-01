FROM registry.saas.hand-china.com/tools/pinpoint-agent
ADD hmall-od-service.jar app.jar
ADD boot.sh boot.sh
RUN sh -c 'chmod +x /boot.sh'
RUN sh -c 'touch /app.jar'
RUN mkdir -p /var/hmall_home/publicfiles/hmall/order
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "/boot.sh" ]