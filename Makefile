all:
	make build-all
	make send-all

build-all:
	make build-payara
	make build-wf

build-payara:
	cd services/agency_service && make build
	cp services/agency_service/target/agency_service.war target
build-wf:
	cd services/flat_service && make build
	cp services/flat_service/target/flat_service.war target

send-all:
	make send-wf-hell
	make send-payara-hell

send-wf-hell:
	scp -P 2222 target/flat_service.war s368601@se.ifmo.ru:~/
send-payara-hell:
	scp -P 2222 target/agency_service.war s344094@se.ifmo.ru:~/

