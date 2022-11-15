### TODO

#### Code

- [ ] Static/Dynamic correlation source code changes
- [X] Multi-Factor Authentication source code changes
- [X] Checkout workflow for Selenium scripts 
- [ ] Login workflow for Selenium scripts
- [X] Product search filter - presentation
- [ ] Product search filter - implementation
- [X] Allow login by email address instead of username
- [X] Complete registration process with email validation  
- [X] Fix the following:
```aidl
com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (through reference chain: com.microfocus.example.payload.response.ApiStatusResponse["timestamp"])
```
- [X] Add "A01:2021-Broken Access Control" example
- [ ] Add "A02:2021-Cryptographic Failures" example
- [X] Add "A03:2021-Injection" - Cross site scripting (Reflected) example  
- [X] Add "A03:2021-Injection" - SQL Injection example
- [ ] Add "A04:2021-Insecure Design" example
- [ ] Add "A05:2021-Security Misconfiguration" example
- [ ] Add "A06:2021-Vulnerable and Outdated Components" example
- [ ] Add "A07:2021-Identification and Authentication Failures" example
- [ ] Add "A08:2021-Software and Data Integrity Failures" example
- [ ] Add "A09:2021-Security Logging and Monitoring Failures" example
- [ ] Add "A10:2021-Server-Side Request Forgery" example

#### Configuration

- [X] Work out why SQL Injection(s) are not listed as CRITICAL
- [ ] Tidy up Maven pom.xml
- [ ] Work out cause of following error in Static Scan:
```aidl
[rulescript] [script: Spring_EL_resolution, version: 6.40] Spring_EL_resolution line 415: TypeError: Cannot call method "indexOf" of undefined
[rulescript]         var pathindex = viewname.indexOf('{');
[rulescript]    at Spring_EL_resolution:415
```
- [X] Include log4j dependency vulnerabilities (by using affected version of log4j)
- [ ] Include SCA/Sonatype affected dependency ? 
- [ ] Azure Resource Manager deployment
- [ ] Migrate pipelines to fcli

#### Data

- [ ] Additional product data

#### Documentation

- [X] Add Vulnerabilities file to illustrate security vulnerabilities aligned to OWASP Top Ten
- [X] Document "A01:2021-Broken Access Control" example
- [ ] Document "A02:2021-Cryptographic Failures" example
- [X] Document "A03:2021-Injection" - Cross site scripting (Reflected) example
- [X] Document "A03:2021-Injection" - SQL Injection example
- [ ] Document "A04:2021-Insecure Design" example
- [ ] Document "A05:2021-Security Misconfiguration" example
- [ ] Document "A06:2021-Vulnerable and Outdated Components" example
- [ ] Document "A07:2021-Identification and Authentication Failures" example
- [ ] Document "A08:2021-Software and Data Integrity Failures" example
- [ ] Document "A09:2021-Security Logging and Monitoring Failures" example
- [ ] Document "A10:2021-Server-Side Request Forgery" example
- [ ] Migrate documentation to Gradle use instead of Maven