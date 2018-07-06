#!/bin/bash

mkdir -p ~/.gradle

echo "trusonaUsername=${ARTIFACTORY_USERNAME}" >> ~/.gradle/gradle.properties
echo "trusonaPassword=${ARTIFACTORY_PASSWORD}" >> ~/.gradle/gradle.properties

echo "ossrhUsername=${OSS_RH_USERNAME}" >> ~/.gradle/gradle.properties
echo "ossrhPassword=${OSS_RH_PASSWORD}" >> ~/.gradle/gradle.properties

echo "signingKeyId=${SIGNING_KEY_ID}" >> ~/.gradle/gradle.properties
echo "signingPassword=${SIGNING_PASSWORD}" >> ~/.gradle/gradle.properties
echo "signingSecretKeyRingFile=${SIGNING_KEY_RING_FILE}" >> ~/.gradle/gradle.properties
