@echo off
cd /d %~dp0
cd ..
set m=src\main
set r=..
iF NOT EXIST %m% mkdir %m%

xcopy %r%\dlz.framework\%m%\* %m% /e/y/Q
xcopy %r%\dlz.web\%m%\* %m% /e/y/Q
xcopy %r%\dlz.web.ssme\%m%\* %m% /e/y/Q

iF EXIST target rd /s /q target
@call mvn -f pom-ssme-all.xml clean source:jar install -Dmaven.test.skip -offline

iF NOT EXIST lib mkdir lib
copy target\*.jar lib\
rd /s /q src
rd /s /q target

pause