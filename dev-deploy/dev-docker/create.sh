#!/bin/bash

VERSION=7.1.0
REPOSITORY=mhus/dev-playground

if [  ! -f Dockerfile ]; then
  echo "not a docker configuration"
  return 1
fi


if [ "$1" = "clean" ]; then
    shift
    docker rmi $REPOSITORY:$VERSION
    docker build --no-cache -t $REPOSITORY:$VERSION .
else
    docker build -t $REPOSITORY:$VERSION .
fi

if [ "$1" = "push" ]; then
    docker push "$REPOSITORY:$VERSION"
fi
