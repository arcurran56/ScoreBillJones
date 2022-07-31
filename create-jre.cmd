set java_home="C:\Program Files\Java\jdk-12.0.2"

rem %java_home%\bin\jdeps^
rem  -R^
rem  --multi-release 12^
rem --system %java_home%^
rem  target\ScoreBillJones-1.0-SNAPSHOT-jar-with-dependencies.jar

%java_home%\bin\jlink^
 --output jre^
 --add-modules java.desktop,java.logging,java.management,java.naming

target\jre\bin\java -jar target\ScoreBillJones-1.0-SNAPSHOT-jar-with-dependencies.jar
