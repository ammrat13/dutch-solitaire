#!/bin/bash

# Builds and runs the program if the build succeeds

cd src

javac Main.java -d ../out -Xdiags:verbose

if [[ $? == 0 ]]
then
	cd ../out
	java Main
fi

cd ..