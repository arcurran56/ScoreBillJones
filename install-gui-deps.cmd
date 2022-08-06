set mvn_home="C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2019.2\plugins\maven\lib\maven3"
set m2_repo=C:\Users\arcur\.m2\repository
set idea_home=C:/PROGRA~1/JetBrains/INTELL~1.2
set java_home=C:/Program Files/Java/jdk-17.0.2
%mvn_home%\bin\mvn -e -X^
  install:install-file^
 -Dfile=%idea_home%/lib/forms_rt.jar^
 -DgroupId=com.intellij^
 -DartifactId=forms_rt^
 -Dversion=2022.1.3^
 -Dpackaging=jar
 -DlocalRepositoryPath=%m2_repo%
