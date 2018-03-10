#!/bin/bash

if [[ $TRAVIS_OS_NAME == 'osx' ]]; then
	./gradlew ios:build
else
	./gradle android:build
fi