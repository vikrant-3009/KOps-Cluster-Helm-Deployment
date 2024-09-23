FROM tomcat:jre17-temurin-jammy

ARG NEXUS_USERNAME
ARG NEXUS_PASSWORD
ARG NEXUS_URL
ARG NEXUS_REPOSITORY

RUN curl -u "${NEXUS_USERNAME}:${NEXUS_PASSWORD}" \
    -o /usr/local/tomcat/webapps/calculator-app.war \
    "${NEXUS_URL}/repository/${NEXUS_REPOSITORY}/com/example/CalculatorApp-SpringBoot/0.0.1/CalculatorApp-SpringBoot-0.0.1.war"