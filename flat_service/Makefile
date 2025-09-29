WF_HOME=../devenv/wildfly-37.0.1.Final

deploy:
	cp target/flat_service.war $(WF_HOME)/standalone/deployments
	$(WF_HOME)/bin/standalone.sh
undeploy:
	rm $(WF_HOME)/standalone/deployments/flat_service*
build:
	mvn clean package -DskipTests
redeploy:
	make build && make undeploy && make deploy

