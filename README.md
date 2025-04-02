# TrekUl

## Maven commands
Tests:
- `mvn test`
- `mvn integration-test`
- `mvn verify`
- Coverage: `mvn test --fail-at-end jacoco:prepare-agent verify jacoco:report-aggregate`

Checkstyle & formatting:
- `mvn spotless:apply`
- `mvn checkstyle:check`


## Checkstyle & formatting
- Important rules can be found in the folder `checkstyle`
  - Mostly based from Google for Checkstyle
- Also using spotless with cleanthat for auto formatting.
  - Rules can be found in the pom.xml
  - Mostly based from [SafeAndConsensual](https://github.com/solven-eu/cleanthat/blob/master/java/src/main/java/eu/solven/cleanthat/engine/java/refactorer/mutators/composite/SafeAndConsensualMutators.java)

## API Documentation
The API documentation is automatically generated during the compilation of the project. To access the documentation, follow these steps:

- Compile the Project
  - Ensure the project is successfully compiled.
- Locate the Documentation
  - Find the `index.html` file in the `doc` folder at the root of the project.
- Open in Browser
  - Open the index.html file in your web browser to view the API documentation.

Make sure the project is running to access the latest documentation updates.

## Project Setup
### Adding the Hebergement XML File

To ensure proper functionality, the file `hebergement.xml` respecting [this Schema](https://donnees.tourisme.gouv.qc.ca/donnees/mto_do_schema_xml.xsd) must be placed at the root of the project directory.
This file is essential for parsing and saving data related to the hosting within the application.