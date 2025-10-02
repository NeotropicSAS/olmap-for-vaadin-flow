# Open Layers Component for Vaadin Flow

Adaptation of `olmap` web component for the Vaadin Flow platform

## License

Apache License 2.0

## Requirements

- Java 21
- Apache Maven 3.9+

## Installation

Install the component in your local repository

```bash
    mvn clean install --DskipTests
```

Use the following maven dependency in your project

```bash
    <dependency>
        <groupId>com.neotropic.flow.component</groupId>
        <artifactId>olmap-for-vaadin-flow</artifactId>
        <version>${project.version}</version>
    </dependency>
```

## Starting the test/demo server:

1. Navigate to demo directory
2. Run `mvn spring-boot:run`.
3. Open http://localhost:8080 in your browser.

## Disclaimer

Open Layers and Vaadin trademarks are property of their respective owners. Neotropic SAS is not affiliated in any way to these companies. Use this component at your own risk.