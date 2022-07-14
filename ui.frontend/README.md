## Description

Module contains Front-End for Howlite. During build it copy build results to web root folder in ui.apps.

## Usage:

Build

```
mvn clean install
```

Local development

Run `npm run sync` to watch your changes. Those will be deployed to local environment on the fly

Local deployment

Front-End files are deployed as a part of `ui.apps` package.

1. Build this module
2. Build with local deployment `ui.apps` module
