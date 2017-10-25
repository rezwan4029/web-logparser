rm -rf target
mvn clean package
cp src/main/resources/access.log target/
cd target
java -cp "parser.jar" com.ef.Parser --accesslog=/Users/rezwan/Documents/monstar-lab/parser/src/main/resources/access.log --startDate=2017-01-01.15:00:00 --duration=hourly --threshold=200
echo "---------------------------------------------------"
java -cp "parser.jar" com.ef.Parser --accesslog=/Users/rezwan/Documents/monstar-lab/parser/src/main/resources/access.log --startDate=2017-01-01.00:00:00 --duration=daily --threshold=500
cd ..

