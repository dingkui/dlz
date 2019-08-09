cd /d %~dp0
cd ..
call mvn -f pom-all.xml clean source:jar install -Dmaven.test.skip -offline
pause
