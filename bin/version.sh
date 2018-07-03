#!/usr/bin/env bash
# Sets the project version
# Usage: ./bin/version.sh [version]


usage() {
  echo "${0} [version]"
  exit
}

if [ $# -ne 1 ]; then
  usage
fi

VERSION=$1

if [ ! -f build.gradle ]; then
  echo "Cannot find build.gradle. Make sure you run this from the root of the repo."
  exit
fi

if [[ "${VERSION}" =~ ^[0-9]*\.[0-9]*\.[0-9]*$ ]]; then
  echo "Setting version to ${VERSION}"
else
  echo "We use Semantic Versioning. Please use the format X.Y.Z"
  exit
fi

sed -i '' "s/[0-9]*\.[0-9]*\.[0-9]*-SNAPSHOT/${VERSION}-SNAPSHOT/" build.gradle
sed -i '' "s/com.trusona:trusona-sdk:[0-9]*\.[0-9]*\.[0-9]*/com.trusona:trusona-sdk:${VERSION}/" README.md