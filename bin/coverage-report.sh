#!/usr/bin/env bash

function coverage {
    name=$1

    source_path=${name}/src/main/java
    report_path=${name}/build/reports/jacoco/test/jacocoTestReport.xml
    out_path=coverage/codeclimate.${name}.json

    echo JACOCO_SOURCE_PATH=${source_path} ./cc-test-reporter format-coverage ${report_path} --input-type jacoco -o ${out_path}
}

curl -L https://codeclimate.com/downloads/test-reporter/test-reporter-latest-linux-amd64 > ./cc-test-reporter
chmod +x ./cc-test-reporter

coverage trusona-sdk
coverage trusona-sdk-http
coverage trusona-sdk-resources

./cc-test-reporter sum-coverage coverage/codeclimate.*.json -p 3

if [[ "$TRAVIS_TEST_RESULT" == 0 ]]; then
  ./cc-test-reporter upload-coverage
fi