WF_HOME=../devenv/wildfly-37.0.1.Final
STANDALONEXML=$(WF_HOME)/standalone/configuration/standalone.xml

deploy:
	@perl -i.bak -0pe 's/<deployments>.*?flat_service\.war.*?<\/deployments>//gs' $(STANDALONEXML)
	@echo "Deployment block removed from $STANDALONEXML"

	rm -f $(WF_HOME)/standalone/deployments/flat_service.war*
	cp target/flat_service.war $(WF_HOME)/standalone/deployments
	$(WF_HOME)/bin/standalone.sh
build:
	mvn clean package -DskipTests
redeploy:
	make build && make deploy

