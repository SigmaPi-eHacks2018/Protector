#!/bin/bash

if [[ $TRAVIS_OS_NAME == 'linux' ]]; then
	yes | sdkmanager "platforms;android-27"
fi