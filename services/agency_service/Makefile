PAYARA_HOME=../../devenv/payara6

deploy:
	$(PAYARA_HOME)/bin/asadmin deploy target/agency_service.war
build:
	mvn clean package -DskipTests
stop:
	$(PAYARA_HOME)/bin/asadmin disable agency_service
undeploy:
	$(PAYARA_HOME)/bin/asadmin undeploy agency_service
stop-payara:
	$(PAYARA_HOME)/bin/asadmin stop-domain domain1
start-payara:
	$(PAYARA_HOME)/bin/asadmin start-domain domain1
redeploy:
	make build && make undeploy && make deploy
get-docs:
	$(PAYARA_HOME)/glassfish/domains/domain1/logs/server.log
health-ping:
	curl http://localhost:8080/agency/health


