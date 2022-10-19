# Howlite

Howlite is a sample component library for the [WebSight CMS](https://www.websight.io/). You can try it together with a demo site. Check the [Getting Started](https://www.websight.io/getting-started/) page for information on how to run a local instance in 5 minutes. You may find an [authoring guide for the components](https://www.websight.io/guides/authoring/howlite/) handy too.

## Modules
- `core` - components related code and scripts
- `ui.frontend` - front-end build
- `tests` - responsible for the automatic validation of the Howlite components
    - `content` - the minimal set of components and pages used during testing
    - `end-to-end` - end-to-end tests validating both Howlite components on authoring and publication

## Development

### Build

```bash
./mvnw clean install
```

### Running end-to-end tests

```bash
./mvnw clean install -P e2e
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
Please read our [Contributing Guide](./CONTRIBUTING.md) before submitting a Pull Request to the project.

## Community support
Please check the community support section in [WebSight Starter](https://github.com/websight-io/starter).

## Authoring Guides
Description of all Howlite components can be found on [websight.io](https://www.websight.io/guides/authoring/howlite/)

## License
Howlite components is `open-source` project with `Apache License 2.0` license.