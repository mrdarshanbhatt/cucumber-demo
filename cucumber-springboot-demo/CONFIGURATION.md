# Maven Profiles Configuration Guide

## Running Tests with Different Profiles

### Development Environment
```bash 
   mvn test -Dspring.profiles.active=dev
```

### Staging Environment  
```bash 
  mvn test -Dspring.profiles.active=staging
```

### Production Environment
```bash
  mvn test -Dspring.profiles.active=prod
```

## Environment-Specific Properties
- dev: test-dev.properties
- staging: test-staging.properties  
- prod: test-prod.properties

## Configuration Override
System properties can override file properties:
```bash
  mvn test -Dapi.base.url=https://custom.api.com -Dapi.timeout=60000
```