#!/bin/bash

if [[ $TRAVIS_OS_NAME == 'osx' ]]; then
	set -euv

    brew update
    brew outdated caskroom/cask/brew-cask || brew upgrade caskroom/cask/brew-cask

    brew tap caskroom/versions
    sudo rm -rf /Library/Java/JavaVirtualMachines
    brew cask install caskroom/versions/java7

    /usr/libexec/java_home --failfast --version 1.7
fi