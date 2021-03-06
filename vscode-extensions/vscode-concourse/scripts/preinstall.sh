#!/bin/bash
set -e

workdir=`pwd`

# Download yaml TextMate grammar
curl https://raw.githubusercontent.com/textmate/yaml.tmbundle/master/Syntaxes/YAML.tmLanguage > yaml-support/yaml.tmLanguage

# Preinstall commons-vscode package
(cd ../commons-vscode ; npm install ; npm pack)
npm install ../commons-vscode/commons-vscode-*.tgz

# Use maven to build fat jar of the language server
cd ../../headless-services/concourse-language-server
./build.sh

mkdir -p ${workdir}/jars
cp target/*.jar ${workdir}/jars/language-server.jar

