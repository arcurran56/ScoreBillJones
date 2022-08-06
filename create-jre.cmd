set java_home="C:\Program Files\Java\jdk-17.0.2"

rem %java_home%\bin\jdeps^
rem  -R^
rem  --multi-release 17^
rem --system %java_home%^
rem  target\ScoreBillJones-1.0-SNAPSHOT-jar-with-dependencies.jar

rmdir /s /q jre

%java_home%\bin\jlink^
 --output jre^
 --add-modules java.desktop,java.logging,java.management,java.naming

jre\bin\java -jar target\ScoreBillJones-2.1-SNAPSHOT-jar-with-dependencies.jar
