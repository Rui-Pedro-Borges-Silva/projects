#!/bin/bash

# Stop execution if a step fails
set -e

IMAGE_NAME=git.fe.up.pt:5050/lbaw/lbaw2324/lbaw2371 # Replace with your group's image name

# Ensure that dependencies are available
composer install
php artisan config:clear
php artisan clear-compiled
php artisan optimize

# docker buildx build --push --platform linux/amd64 -t $IMAGE_NAME .
sudo docker build -t $IMAGE_NAME .
sudo docker push $IMAGE_NAME
