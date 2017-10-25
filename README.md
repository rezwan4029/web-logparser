# web-logparser
Log parsing application

Steps to run 
-------------

Build project :
```bash 
mvn clean package
```
Run command :
```bash
java -cp "parser.jar" com.ef.Parser --accesslog=src/main/resources/access.log --startDate=2017-01-01.15:00:00 --duration=hourly --threshold=200

```
