 cd .. && make build-payara
 mv target/agency_service.war locallol/ && \
 cd - && \
 java \
  -Djavax.net.ssl.keyStore=/Users/jaba/IdeaProjects/soa/services/agency_service/keystore.p12 \
  -Djavax.net.ssl.keyStorePassword=password \
  -Djavax.net.ssl.keyStoreType=PKCS12 \
  -Dfish.payara.ssl.port=23223 \
  -jar payara-micro-6.2024.3.jar \
    --nocluster \
    --addlibs postgresql-42.7.8.jar \
    --addlibs cassandra-libs \
    --postbootcommandfile db-configuration-helios.asadmin \
    --deploy agency_service.war \
    --sslPort 23223
