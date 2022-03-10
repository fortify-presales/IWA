#!/bin/sh
echo "Building IWA container"
docker build -f src/main/configs/Dockerfile -t iwa:latest .
