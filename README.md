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
After building the project, start MongoDB:

```bash
docker run -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=mongoadmin -e MONGO_INITDB_ROOT_PASSWORD=mongoadmin mongo:4.4.6
```


and run howlite-test feature using Sling Launcher from `tests/end-to-end` directory:

```bash
java -jar target/dependency/org.apache.sling.feature.launcher.jar -f target/slingfeature-tmp/feature-howlite-tests.json
```

Instance should start at http://localhost:8080/ in a couple of seconds (default credentials: `wsadmin/wsadmin`).

## Contributing
See [CONTRIBUTING.md](./CONTRIBUTING.md).

## Authoring Guides
Description of all Howlite components can be found on [websight.io](https://www.websight.io/guides/authoring/howlite/)