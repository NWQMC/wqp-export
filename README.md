# WQP-EXPORT

Microservice or CMD-Line Tool
================================

A tiny microservice to export the WQP WebSerive Log to CSV FILE

## Install

This project uses maven build for jar or war (war is default).

```sh
# spring-boot build
$ maven package -P jar
```

## Usage

### WAR MODE

```sh
# for INFO level debug spring-boot launch
$ ./boot

# for DEBUG level (and other dev env settings) launch
$ ./boot dev

# shutdown procedure is simply
./shutdown

# if the app.pid does not exist or is wrong
./stop
# kills all spring-boot
```


* http://localhost:8777/
* http://localhost:8777/wqp-export/
	both return up status of service
* http://localhost:8777/wqp-export/version
	returns the build version page
* http://localhost:8777/wqp-export/input
	returns a data test input page
* http://localhost:8777/wqp-export/count
	the restful service end to get the record count only
* http://localhost:8777/wqp-export/export
	returns the CSV export data
* http://localhost:8777/wqp-export/fileExport
    update the CSV export data file


### JAR MODE

```sh
# spring-boot build
$ maven package -P jar
$ unzip -p target/wqp-export-1.0.0-SNAPSHOT.jar  BOOT-INF/classes/commands/export > export
$ ./export [filename] [db-server] [db-password]  <optional-username>
or
$ java -jar target\wqp-export-[version].jar [filename] [db-server] [db-password] <optional-username>
```

The file name is optional. the default file is wqp-web-log.csv at the time of this readme.
See the applicaiton.properties export.filename for updates.
