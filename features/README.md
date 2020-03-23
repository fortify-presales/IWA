This directory contains files indicating possible features that are invoked from the Jenksfile build.
To enable a feature create a file with one of the following names:

 - _sca.enabled_    - enable static application security testing using local Fortify SCA toolset
 - _fod.enabled_    - enable static application security testing using Fortify on Demand (FOD)
 - _ssc.enabled_    - upload local scan results (FPR) to Fortify Software Security Center (SSC)
 - _da.enabled_     - enable local deployment of web application to/using Deployment Automation
 - _wi.enabled_     - enable local WebInspect dynamic application security testing (after application deployment)
