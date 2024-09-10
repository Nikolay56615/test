#!/bin/bash

# Let's sure that the directory for the classes exists
if [ ! -d "out" ]; then
  mkdir out
fi

# Compilation of all .java files in the directory
javac -d out --release 8 src/main/java/ru/nsu/lebedev/*.java

# JavaDoc Generation
javadoc -d doc src/main/java/ru/nsu/lebedev/*.java

# Creating a manifest for a JAR file
echo "Main-Class: ru.nsu.lebedev.Main" > manifest.txt

# Creating a JAR file specifying the manifest
jar cvfm app.jar manifest.txt -C out .

# Deleting a temporary manifest
rm manifest.txt

# Launching the program
java -cp app.jar ru.nsu.lebedev.Main
