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

## SQL problems

[Solution 1](https://github.com/rezwan4029/web-logparser/blob/master/src/main/resources/sql/sql_solution_1.sql)

[Solution 2](https://github.com/rezwan4029/web-logparser/blob/master/src/main/resources/sql/sql_solution_2.sql)
