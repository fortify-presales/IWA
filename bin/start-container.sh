#!/bin/sh
echo "Starting IWA container"
docker run -d --restart always -p 8088:8080 --name iwa iwa:latest
