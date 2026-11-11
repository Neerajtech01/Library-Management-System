@echo off
if not exist out mkdir out
javac -d out src\main\java\librarymanagement\*.java src\main\java\librarymanagement\model\*.java src\main\java\librarymanagement\service\*.java src\main\java\librarymanagement\ui\*.java src\main\java\librarymanagement\util\*.java
if errorlevel 1 pause & exit /b 1
java -cp out librarymanagement.Main
