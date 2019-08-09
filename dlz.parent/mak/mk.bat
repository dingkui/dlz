cd /d %~dp0
cd ..
call mvn -f pom-all.xml clean install -Dmaven.test.skip
pause