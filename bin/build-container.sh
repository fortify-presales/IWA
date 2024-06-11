#!/bin/sh
echo "Building IWA container"
docker build -f Dockerfile -t iwa:latest .