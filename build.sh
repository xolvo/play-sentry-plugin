cd ./sentry
play deps --sync
play build-module

cd ../simple-app
play deps --sync
play ec
