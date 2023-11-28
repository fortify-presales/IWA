$RootPath = Split-Path $PSScriptRoot -Parent
Write-Host $RootPath
$compress = @{
    Path = "$($RootPath)\etc\*.yml", "$($RootPath)\etc\*.json", "$($RootPath)\docker-compose.*", "$($RootPath)\Docker*.*", "$($RootPath)\.github\workflows\*.yml"
    CompressionLevel = "Fastest"
    DestinationPath = "$($RootPath)\infrastructure.zip"
}
Compress-Archive @compress -Verbose
