#!/bin/bash

SONAR_VERSION="sonar-scanner-cli-3.0.3.778"
SONAR_DIR="sonar-scanner-3.0.3.778"

wget -P $HOME -N "https://sonarsource.bintray.com/Distribution/sonar-scanner-cli/${SONAR_VERSION}.zip"

unzip -d $HOME $HOME/$SONAR_VERSION.zip

if [ -n "$CI_PULL_REQUEST" ]; then

  echo "Preview analyzing ${CI_PULL_REQUEST} by SonarQube Github Plugin"

  mvn clean install -Dmaven.test.skip=true

  $HOME/$SONAR_DIR/bin/sonar-scanner -X $SONAR_COMMON_PROJECT_INFO \
    -Dsonar.login=${SONAR_TOKEN} \
    -Dsonar-project.properties=utf-8 \
    -Dsonar.github.repository=$CIRCLE_PROJECT_USERNAME/$CIRCLE_PROJECT_REPONAME \
    -Dsonar.github.pullRequest=${CI_PULL_REQUEST##*/} \
    -Dsonar.github.oauth=$GITHUB_TOKEN \
    -Dsonar.projectKey=${CIRCLE_PROJECT_REPONAME} \
    -Dsonar.projectName=${CIRCLE_PROJECT_REPONAME} \
    -Dsonar.projectVersion=${CIRCLE_BRANCH} \
    -Dsonar.java.source=1.8 \
    -Dsonar.java.target=1.8 \
    -Dsonar.analysis.mode=preview;
fi


if [ ${CIRCLE_BRANCH} == 'master' ]; then
  BRANCH_PREFIX=${CIRCLE_BRANCH}
else
  BRANCH_PREFIX=`echo ${CIRCLE_BRANCH} |awk -F "/" '{print $1}'`
fi

if [ $BRANCH_PREFIX == 'master' ] || [ $BRANCH_PREFIX == 'release' ]; then
    mvn clean install -Dmaven.test.skip=true sonar:sonar \
      -Dsonar.host.url=http://127.0.0.1:9000 -Dsonar.login=${SONAR_TOKEN} \
      -Dsonar.sourceEncoding=UTF-8 \
      -Dsonar.projectKey=${CIRCLE_PROJECT_REPONAME} \
      -Dsonar.projectName=${CIRCLE_PROJECT_REPONAME} \
      -Dsonar.projectVersion=${CIRCLE_BRANCH} \
      -Dsonar.java.source=1.8 \
      -Dsonar.java.target=1.8
fi
