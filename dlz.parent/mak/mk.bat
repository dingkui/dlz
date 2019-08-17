cd /d %~dp0
cd ..
call mvn -f pom.xml clean install -Dmaven.test.skip
pause