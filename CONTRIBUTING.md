# How to contribute

- [Contributing code](#contributing-code)

## Contributing code

### Coding standards

Follow Google Style Guide code formatting, particularly set your IDE `tab size`/`ident` to 2 spaces 
and `continuation ident` to 4 spaces.
  - [Google Style Guide for Eclipse](https://raw.githubusercontent.com/google/styleguide/gh-pages/eclipse-java-google-style.xml)
  - [Google Style Guide for IntelliJ](https://raw.githubusercontent.com/google/styleguide/gh-pages/intellij-java-google-style.xml)

Additionally, we use the `maven-checkstyle-plugin` plugin to validate all rules, so if there is some
checkstyle issue, our `mvn clean install` should fail with the message:

```bash
[INFO] Starting audit...
[WARN] /Projects/websight/projects/howlite/core/src/main/java/pl/ds/howlite/components/models/AccordionItemComponent.java:12:8: 'member def modifier' has incorrect indentation level 7, expected level should be 2. [Indentation]
[WARN] /Projects/websight/projects/howlite/core/src/main/java/pl/ds/howlite/components/models/AccordionItemComponent.java:13:3: Annotation 'Inject' have incorrect indentation level 2, expected level should be 7. [AnnotationLocationVariables]
[WARN] /Projects/websight/projects/howlite/core/src/main/java/pl/ds/howlite/components/models/AccordionItemComponent.java:14:3: Annotation 'Default' have incorrect indentation level 2, expected level should be 7. [AnnotationLocationVariables]
Audit done.
```

## Releasing
Howlite release is done using GitHub Actions `Release Howlite` workflow.

The 'Prepare release' step requires configuring:
- a public key as [deploy keys](https://docs.github.com/v3/guides/managing-deploy-keys/#deploy-keys) 
- a private key as a GitHub Action secret (`SSH_SECRET_KEY`).
