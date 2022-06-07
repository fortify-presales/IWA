#!/bin/sh
cd /usr/share/repos/IWAPharmacyDirect
docker-compose down
git pull
mvn clean package
ln -s ./src/main/configs/docker-compose.yml .
docker build -f src/main/configs/Dockerfile -t iwa:latest .
docker login
docker tag iwa:latest mfdemouk/iwa:latest
docker push mfdemouk/iwa:latest
docker-compose up -d