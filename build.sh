#!/usr/bin/env bash

cd ./sentry
play deps --sync
play build-module

cd ../simple-app
play deps --sync
