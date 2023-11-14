Write-Host "Building IWA container"
docker build -f .\Dockerfile.win -t iwa:latest .
