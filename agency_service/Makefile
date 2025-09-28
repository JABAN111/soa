PAYARA_HOME=../devenv/payara6

deploy:
	$(PAYARA_HOME)/bin/asadmin deploy target/agency_service.war
build:
	mvn clean package -DskipTests
stop:
	$(PAYARA_HOME)/bin/asadmin disable agency_service
undeploy:
	$(PAYARA_HOME)/bin/asadmin undeploy agency_service
stop-paraya:
	$(PAYARA_HOME)/bin/asadmin stop-domain domain1
start-paraya:
	$(PAYARA_HOME)/bin/asadmin start-domain domain1