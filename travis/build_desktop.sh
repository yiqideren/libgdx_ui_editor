#!/usr/bin/env bash
if [[ $TRAVIS_PULL_REQUEST == 'false' && $TRAVIS_REPO_SLUG == 'whitecostume/libgdx_ui_editor' && $TRAVIS_BRANCH == 'master' ]];
then
    gradlew desktop:dist
fi