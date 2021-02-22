#
# Example script to perform Fortify Source and Lib Scanning with Sonatype
#

    [CmdletBinding()]
param (
    [Parameter(Mandatory)]
    [string]$NexusIQUrl,

    [Parameter(Mandatory)]
    [string]$NexusIQAuth,

    [Parameter(Mandatory)]
    [string]$NexusIQAppId,

    [Parameter(Mandatory)]
    [string]$SSCURL,

    [Parameter(Mandatory)]
    [string]$SSCAuthToken,

    [Parameter(Mandatory)]
    [string]$SSCAppVersionId
)
begin {
    # Check Maven is on the path
    if ((Get-Command "mvn.cmd" -ErrorAction SilentlyContinue) -eq $null)
    {
        Write-Error "Unable to find mvn.cmd in your PATH"
        Break
    }

    # Check Source Analyzer is on the path
    if ((Get-Command "sourceanalyzer.exe" -ErrorAction SilentlyContinue) -eq $null)
    {
        Write-Error "Unable to find sourceanalyzer.exe in your PATH"
        Break
    }

    # Check Source And Lib Analyzer is installed
    if ((Get-Command "sourceandlibscanner.bat" -ErrorAction SilentlyContinue) -eq $null)
    {
        Write-Error "sourceandlibscanner.bat is not in your PATH"
        Break
    }
}
process {
    # Clean Project and scan results from previous run
    Write-Host ************************************************************
    Write-Host Cleaning project and scan results from previous run...
    Write-Host ************************************************************
    & mvn clean
    & sourceanalyzer -Dcom.fortify.sca.ProjectRoot=.fortify -b $NexusIQAppId -clean

    # Scan the application
    Write-Host ************************************************************
    Write-Host Scanning source files and libs ...
    Write-Host ************************************************************
    & sourceandlibscanner -auto -scan -bt mvn -bf pom.xml  `
        -bc 'dependency:unpack-dependencies -Dcom.fortify.sca.ProjectRoot=.fortify -Dclassifier=sources -DexcludeTransitive -DskipTests package' `
        -sonatype -iqurl $NexusIQUrl -nexusauth $NexusIQAuth -iqappid $NexusIQAppId -stage build -r iqReport.json -f ${NexusIQAppId}.json `
        -upload -ssc $SSCURL -ssctoken $SSCAuthToken -versionid $SSCAppVersionId
}
end {

}
