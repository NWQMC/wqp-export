#!/bin/bash

echo ""

if [[ $1 = "--help" ]]; then
	echo "Usage: $0 [dev|prod]"
	echo ""
	echo "      This script stops any running spring-boot, rebuilds service with maven,"
	echo "      and then launches the service spring-boot microservice."
	echo "      It puts its PID in the app.pid file."
	echo "      Please do not delete the file while the service is running."
	echo ""
	echo "      dev  - indicates to spring to use application-dev.properties while"
	echo "      prod - uses application.properties and prod is the default for this script."
	echo "      it is not recommended that dev be used in prod but the reverse is ok."
	echo ""
	echo "      --help prints this message"
	echo ""
	exit
fi

echo "Building and Starting Service..."

echo "Checking necessary paths"
mkdir -p logs

./shutdown

echo "Maven Building..."
mvn package -P jar --log-file logs/mvn.log

echo "Starting..."
java -jar target/WQP-Export-1.0.0-SNAPSHOT.jar service --spring.profiles.active=${1:-prod} > logs/wqp-export.log 2>logs/error.log &
echo $! > app.pid

echo "Service Started."
echo ""
