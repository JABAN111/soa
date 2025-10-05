##TODO fix doesn't work
agency_dir="../services/agency_service"



start-payara: # start payara and deploy application(without build)
	$(MAKE) -C $(agency_dir) stop-payara
	$(MAKE) -C $(agency_dir) start-payara
	$(MAKE) -C $(agency_dir) undeploy
	$(MAKE) -C $(agency_dir) deploy

start-wildfly:

start-all: # start payara && wildfly without building

