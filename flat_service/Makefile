WF_HOME=../devenv/wildfly-37.0.1.Final

# NOTE: doesn't work if u preconfigure via idea.
deploy:
	rm -f $(WF_HOME)/standalone/deployments/flat_service.war*
	rm -rf $(WF_HOME)/standalone/data
	rm -rf $(WF_HOME)/standalone/configuration/standalone_xml_history
	cp target/flat_service.war $(WF_HOME)/standalone/deployments
	$(WF_HOME)/bin/standalone.sh
undeploy:
	rm $(WF_HOME)/standalone/deployments/flat_service*
build:
	mvn clean package -DskipTests
redeploy:
	make build && make undeploy && make deploy

