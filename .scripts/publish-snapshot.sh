#!/bin/sh

if [ "$TRAVIS_REPO_SLUG" == "talares/talares" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then

  echo "Publishing SNAPSHOT to Sonatype repositories..."

  { sbt publish; };
fi