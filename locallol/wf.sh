rm wildfly-37.0.1.Final/standalone/deployments/flat_service*
cd .. && make build-wf && cp target/flat_service.war locallol && cd -
cp flat_service.war wildfly-37.0.1.Final/standalone/deployments/
export _JAVA_OPTIONS="-Xmx1G"
wildfly-37.0.1.Final/bin/standalone.sh -b 0.0.0.0 -Djboss.http.port=23210 -Djboss.https.port=23211 -Djboss.management.http.port=23209
