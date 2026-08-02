## ADDED Requirements

### Requirement: Reusable GitHub Action Workflow
The system SHALL provide a GitHub Action workflow file that can be called by other workflows.

#### Scenario: Workflow invocation
- **WHEN** a dependent repository triggers the workflow using `uses:`
- **THEN** the workflow executes its defined jobs with the provided `java-version` and `build-tool` inputs.

### Requirement: Build Tool Auto-Detection
The workflow SHALL inspect the repository root to determine if the project uses Maven or Gradle when the `build-tool` input is set to `auto`.

#### Scenario: Maven auto-detection
- **WHEN** `pom.xml` is present
- **THEN** the workflow detects Maven and builds using `mvn clean package`.

#### Scenario: Gradle auto-detection
- **WHEN** `build.gradle` or `build.gradle.kts` is present
- **THEN** the workflow detects Gradle and builds using `./gradlew build`.

### Requirement: Plugin Artifact Isolation
The workflow SHALL isolate the primary compiled JAR file from secondary JARs like sources or javadocs.

#### Scenario: Finding the main JAR
- **WHEN** the build finishes
- **THEN** it finds a `.jar` file excluding `*-sources.jar` and `*-javadoc.jar`.

### Requirement: Upload to Release
The workflow SHALL upload the identified JAR file to the GitHub Release that triggered it.

#### Scenario: Uploading asset
- **WHEN** the JAR is located
- **THEN** it executes `gh release upload` targeting the current tag name with the JAR path.
