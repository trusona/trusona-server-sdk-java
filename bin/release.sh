#!/usr/bin/env bash

tmp_file=`mktemp`

if [ ! -f gradlew ]; then
  echo "Cannot find gradle wrapper. Run from repo root."
  exit -1
fi


SNAPSHOT_VERSION=$(./gradlew trusona-sdk:printVersion | grep SNAPSHOT)

if [ "${SNAPSHOT_VERSION}" == "" ]; then
  echo "Version is not a snapshot. Aborting."
  exit -1
fi


GIT_STATUS=$(git status -s)

if [ ! -z "${GIT_STATUS}" ]; then
  echo "Repo is not clean. Resolve the following uncomitted changes:"
  echo ${GIT_STATUS}
  exit -1
fi


RELEASE_VERSION=$(echo ${SNAPSHOT_VERSION} | cut -d- -f1)

BRANCH_NAME="release-${RELEASE_VERSION}"
TAG_NAME="v${RELEASE_VERSION}"

git checkout -b ${BRANCH_NAME}

sed -e "s/${SNAPSHOT_VERSION}/${RELEASE_VERSION}/g" build.gradle > ${tmp_file}
mv ${tmp_file} build.gradle
git add build.gradle

git commit -m "release ${TAG_NAME}"
git tag -f -a ${TAG_NAME} -m "release ${TAG_NAME}"

echo -n "Do you want to publish version ${RELEASE_VERSION} now? [y/N]: "
read RESPONSE

if [ "${RESPONSE}" == "y" ]; then
  git push --set-upstream origin ${BRANCH_NAME}
  git push --set-upstream origin ${TAG_NAME}
else
  echo "Run the following commands to publish release:\n"
  echo git push --set-upstream origin ${BRANCH_NAME}
  echo git push --set-upstream origin ${TAG_NAME}
fi

