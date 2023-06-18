
.PHONY: run-dev
run-dev:
	mv config/application.properties config/backup.application.properties
	cp config/dev.application.properties config/application.properties
#	./gradlew bootRun &
	docker-compose up -d
	mv config/backup.application.properties config/application.properties
