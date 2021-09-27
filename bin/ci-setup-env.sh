#!/usr/bin/env bash

mkdir -p ~/.gradle

echo "trusonaUsername=${ARTIFACTORY_USERNAME}" >> ~/.gradle/gradle.properties
echo "trusonaPassword=${ARTIFACTORY_PASSWORD}" >> ~/.gradle/gradle.properties

echo "ossrhUsername=${OSS_RH_USERNAME}" >> ~/.gradle/gradle.properties
echo "ossrhPassword=${OSS_RH_PASSWORD}" >> ~/.gradle/gradle.properties

echo "signingKeyId=${SIGNING_KEY_ID}" >> ~/.gradle/gradle.properties
echo "signingPassword=${SIGNING_PASSWORD}" >> ~/.gradle/gradle.properties
echo "signingSecretKeyRingFile=${SIGNING_KEY_RING_FILE}" >> ~/.gradle/gradle.properties

TRUSONA=

if [[ "$TRAVIS_PULL_REQUEST_SLUG" =~ ^trusona\/.*$ ]] || [[ "$TRAVIS_REPO_SLUG" =~ ^trusona\/.*$ ]]; then
  export TRUSONA=1
  TRUSONA=1
fi

if [ -n "$TRUSONA" ]; then
  openssl aes-256-cbc -K $encrypted_877b689a489a_key -iv $encrypted_877b689a489a_iv -in keystores/trusona-signing.gpg.enc -out keystores/trusona-signing.gpg -d
fi