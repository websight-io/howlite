# Howlite tests
This module is responsible for the automatic validation of the Howlite components.

It contains:
- [content](./content) - providing the minimal set of components and pages used during testing
- [end-to-end](./end-to-end) - end-to-end tests validating both Howlite components on authoring and publication

## How to run tests without Percy

```bash
mvn clean install -P e2e
```

## How to run tests with Percy
You need to set HOWLITE_PERCY_TOKEN variable to upload snapshot to Percy.
```bash
mvn clean install -P e2e-visual
```

Remember that e2e and e2e-visual are overlaying in some part and should not be run together!

Packs the content package, then builds a Docker image that contains the package. Finally, it runs the container with the created Docker image 
and runs functional tests.