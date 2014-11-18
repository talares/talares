#!/bin/sh

if [ "$TRAVIS_REPO_SLUG" == "talares/talares" ] && [[ "$TRAVIS_SCALA_VERSION" == "2.11"* ]] && [ "$TRAVIS_JDK_VERSION" == "oraclejdk8" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then

  echo -e "Publishing API documentation...\n"

  cp -R ${TRAVIS_BUILD_DIR}/src/talares/target/scala-2.11/api $HOME/scaladoc-latest
  cp -R ${TRAVIS_BUILD_DIR}/src/talares-java/target/scala-2.11/api $HOME/javadoc-latest

  cd $HOME
  git config --global user.email ${GH_EMAIL}
  git config --global user.name ${GH_NAME}
  git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/talares/talares.github.io gh-pages > /dev/null

  cd gh-pages
  git rm -rf ./api/scala
  git rm -rf ./api/java
  cp -Rf $HOME/scaladoc-latest ./api/scala
  cp -Rf $HOME/javadoc-latest ./api/java
  git add -f .
  git commit -m "Latest API documentation for Travis build $TRAVIS_BUILD_NUMBER pushed to gh-pages"
  git push -fq origin gh-pages > /dev/null

  echo -e "Published API documentation to gh-pages.\n"

fi
