### TODO

#### Code

- [ ] Static/Dynamic correlation source code changes
- [ ] Multi Factor Authentication source code changes
- [X] Checkout workflow for Selenium scripts 
- [ ] Login workflow for Selenium scripts
- [X] Product search filter - presentation
- [ ] Product search filter - implementation
- [X] Allow login by email address instead of username
- [ ] Complete registration process with email validation  
- [X] Fix the following:
```aidl
com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (through reference chain: com.microfocus.example.payload.response.ApiStatusResponse["timestamp"])
```
  
#### Configuration

- [ ] Work out why SQL Injection(s) are not listed as CRITICAL
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

#### Data

- [ ] Additional product data

#### Documentation

- [ ] Migrate documentation to Gradle use instead of Maven