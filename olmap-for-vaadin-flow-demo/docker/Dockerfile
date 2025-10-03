FROM alpine:latest

RUN apk add --no-cache openjdk11 npm maven subversion

RUN adduser -D -s /sbin/nologin kuwaiba

RUN mkdir -p /data/logs
RUN mkdir -p /opt/programs/kuwaiba/components

WORKDIR /opt/programs

RUN svn checkout svn://svn.code.sf.net/p/kuwaiba/code/components/olmap-flow/trunk/olmap-flow-root/olmap-flow/ kuwaiba/components/olmap-flow
RUN svn checkout svn://svn.code.sf.net/p/kuwaiba/code/components/olmap-flow/trunk/olmap-flow-root/olmap-flow-demo/ kuwaiba/components/olmap-flow-demo

RUN mvn -f kuwaiba/components/olmap-flow/ clean install
RUN mvn -f kuwaiba/components/olmap-flow-demo/ clean install

RUN mv kuwaiba/components/olmap-flow-demo/target/olmap-flow-demo-0.1.0.jar ./

RUN rm -rf /opt/programs/kuwaiba

RUN apk del npm maven subversion

RUN chown -R kuwaiba:kuwaiba /data
RUN chown -R kuwaiba:kuwaiba /opt/programs

EXPOSE 8080

USER kuwaiba
CMD ["sh", "-c", "java -jar /opt/programs/olmap-flow-demo-0.1.0.jar"]