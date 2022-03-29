#!/bin/sh
echo "Building IWA container"
docker build -f src/main/configs/Dockerfile -t iwa:latest .
#docker tag iwa:latest mfdemouk/iwa:latest
#docker push mfdemouk/iwa:latest
