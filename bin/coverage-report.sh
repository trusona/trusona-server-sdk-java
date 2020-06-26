#!/usr/bin/env bash

function setup_cc {
  if [ ! -f ./cc-test-reporter ]; then

    os=darwin
    if [[ -n $TRAVIS ]]; then
      os=linux
    fi

    curl -L https://codeclimate.com/downloads/test-reporter/test-reporter-latest-${os}-amd64 > ./cc-test-reporter
    chmod +x ./cc-test-reporter
  fi
}

function coverage {
    name=$1

    source_path=${name}/src/main/java
    report_path=${name}/build/reports/jacoco/test/jacocoTestReport.xml
    out_path=coverage/codeclimate.${name}.json

    JACOCO_SOURCE_PATH=${source_path} ./cc-test-reporter format-coverage ${report_path} --input-type jacoco -o ${out_path}
}

function publish {
  ./cc-test-reporter sum-coverage coverage/codeclimate.*.json -p 3

  if [[ "$TRAVIS_TEST_RESULT" == 0 ]]; then
    ./cc-test-reporter upload-coverage
  fi
}


setup_cc

coverage trusona-sdk
coverage trusona-sdk-http
coverage trusona-sdk-resources

publish