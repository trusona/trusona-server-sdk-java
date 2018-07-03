#!/bin/bash

mkdir -p ~/.gradle

echo "trusonaUsername=${ARTIFACTORY_USERNAME}" >> ~/.gradle/gradle.properties
echo "trusonaPassword=${ARTIFACTORY_PASSWORD}" >> ~/.gradle/gradle.properties

