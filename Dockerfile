FROM tomcat:jre17-temurin-jammy

ARG ARTIFACTORY_USERNAME
ARG ARTIFACTORY_PASSWORD
ARG ARTIFACTORY_URL

RUN curl -u "${ARTIFACTORY_USERNAME}:${ARTIFACTORY_PASSWORD}" \
    -o /usr/local/tomcat/webapps/calculator-app-spring-boot.war \
    "${ARTIFACTORY_URL}/java-app-1/dev/calculator-app-spring-boot.war"