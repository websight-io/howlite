# Howlite

Howlite project repository.

## Modules
- `core` - components logic for Howlite
- `ui.apps` - components content for Howlite
- `ui.frontend` - front-end for Howlite
- `tests` - responsible for the automatic validation of the Howlite components
    - `content` - the minimal set of components and pages used during testing
    - `end-to-end` - end-to-end tests validating both Howlite components on authoring and publication
## Development

### Build

```bash
mvn clean install
```

### Running end-to-end tests

```bash
mvn clean install -P e2e
```

### Running dev instance
After building the project, from `tests/end-to-end` directory run:

```
java -jar target/dependency/org.apache.sling.feature.launcher.jar -f target/slingfeature-tmp/feature-howlite_tests_tar.json
```

Instance should start at http://localhost:8080/ in a couple of seconds.

## Contributing
See [CONTRIBUTING.md](./CONTRIBUTING.md).
