@echo off
cd /d %~dp0
cd ..
set m=src\main
set r=..\..
mkdir %m%

xcopy %r%\dlz.framework\%m%\* %m% /e/y/Q
xcopy %r%\dlz.framework.db.springjdbc\%m%\* %m% /e/y/Q

mkdir %m%\java\src\com\dlz\web
mkdir %m%\java\src\com\dlz\web\util
copy %r%\dlz.web\src\main\java\com\dlz\web\util\HttpUtil.java %m%\java\src\com\dlz\web\util


rd /s /q target
@call mvn clean source:jar install -Dmaven.test.skip -offline
rd /s /q %m%
rd /s /q target\classes
rd /s /q target\generated-sources
rd /s /q target\maven-archiver
rd /s /q target\maven-status
pause