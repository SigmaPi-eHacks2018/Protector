#!/bin/bash

if [[ $TRAVIS_OS_NAME == 'osx' ]]; then
	./gradlew ios:build
else
	./gradlew android:build
fi