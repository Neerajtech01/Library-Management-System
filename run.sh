#!/bin/sh
mkdir -p out
javac -d out $(find src/main/java -name '*.java') || exit 1
java -cp out librarymanagement.Main
