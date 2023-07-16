#!/bin/bash

if [[ $# -eq 0 ]] ; then
    echo 'Please provide the following parameters: <old-version> <new-version>'
    exit 1
fi
if [ -z "$1" ]; then
    echo "No old-version supplied."
    exit 1
fi
if [ -z "$2" ]; then
    echo "No new-version supplied."
    exit 1
fi

OLD_VERSION=$1
NEW_VERSION=$2

mvn versions:set -DnewVersion=$NEW_VERSION
mvn versions:commit
sed -i -e "s/$OLD_VERSION/$NEW_VERSION/g" README.md
git add -u
git commit -m "Release $NEW_VERSION"
git push origin
git tag v$NEW_VERSION
git push origin --tags
