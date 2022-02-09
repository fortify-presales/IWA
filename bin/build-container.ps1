Write-Host "Building IWA container"
docker build -f src\main\configs\Dockerfile.win -t iwa:latest .
