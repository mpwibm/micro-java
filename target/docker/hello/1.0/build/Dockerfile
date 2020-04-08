FROM fabric8/s2i-java:2.3
ENV AB_PROMETHEUS_OFF=true JAVA_APP_DIR=/deployments
LABEL org.label-schema.description="Hello microservice using Thorntail" org.label-schema.version=1.0 org.label-schema.schema-version=1.0 org.label-schema.build-date=2020-04-08T13:48:12.482 org.label-schema.name="Red Hat Training Hello Java app" org.label-schema.vcs-ref=03e9fb9e22139a0c612e494fcda02577ee4c1589 org.label-schema.vcs-url=https://github.com/mpwibm/DO288-apps
EXPOSE 8080 8778 9779
COPY maven /deployments/
