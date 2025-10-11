##TODO fix doesn't work
agency_dir="../services/agency_service"



start-payara: # start payara and deploy application(without build)
	$(MAKE) -C $(agency_dir) stop-payara
	$(MAKE) -C $(agency_dir) start-payara
	$(MAKE) -C $(agency_dir) undeploy
	$(MAKE) -C $(agency_dir) deploy

start-wildfly:

start-all: # start payara && wildfly without building

push-wildflyToHell:
	tar --zstd -cf wf.tar.zst  wildfly-37.0.1.Final
	scp -P 2222 soa.tar.zst s368601@se.ifmo.ru:~
	hell
