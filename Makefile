
.PHONY: run-dev
run-dev:
	mv config/application.properties config/backup.application.properties
	cp config/dev.application.properties config/application.properties
#	./gradlew bootRun &
	docker-compose up -d
	mv config/backup.application.properties config/application.properties


.PHONY: sslCertificate
sslCertificate:
	openssl req -newkey rsa:4096 \
              -x509 \
              -sha256 \
              -days 3650 \
              -nodes \
              -out secrets/server.crt \
              -keyout secrets/private.key