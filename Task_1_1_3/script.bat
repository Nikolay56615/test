@echo off

REM Let's sure that the directory for the classes exists
if not exist out mkdir out

REM Compilation of all .java files in the directory
javac -d out --release 8 src\main\java\ru\nsu\lebedev\*.java

REM JavaDoc Generation
javadoc -d doc src\main\java\ru\nsu\lebedev\*.java

REM Creating a JAR file
jar from app.jar ru.nsu.lebedev.Main -C out .

REM Launching the program
java -cp app.jar ru.nsu.lebedev.Main