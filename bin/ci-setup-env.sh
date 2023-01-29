#!/usr/bin/env bash

mkdir -p ~/.gradle

echo "trusonaUsername=__${ARTIFACTORY_USERNAME}" >> ~/.gradle/gradle.properties
echo "trusonaPassword=__${ARTIFACTORY_PASSWORD}" >> ~/.gradle/gradle.properties

echo "ossrhUsername=__${OSS_RH_USERNAME}" >> ~/.gradle/gradle.properties
echo "ossrhPassword=__${OSS_RH_PASSWORD}" >> ~/.gradle/gradle.properties

echo "signingKeyId=__${SIGNING_KEY_ID}" >> ~/.gradle/gradle.properties
echo "signingPassword=__${SIGNING_PASSWORD}" >> ~/.gradle/gradle.properties
echo "signingSecretKeyRingFile=__${SIGNING_KEY_RING_FILE}" >> ~/.gradle/gradle.properties


exit


TRUSONA=

if [[ "$TRAVIS_PULL_REQUEST_SLUG" =~ ^trusona\/.*$ ]] || [[ "$TRAVIS_REPO_SLUG" =~ ^trusona\/.*$ ]]; then
  export TRUSONA=1
  TRUSONA=1
fi

if [ -n "$TRUSONA" ]; then
  openssl aes-256-cbc -K $encrypted_877b689a489a_key -iv $encrypted_877b689a489a_iv -in keystores/trusona-signing.gpg.enc -out keystores/trusona-signing.gpg -d
fi