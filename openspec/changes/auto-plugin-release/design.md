## Context

The current process of publishing a Minecraft plugin requires building the JAR file locally and uploading it manually to GitHub Releases. Given that multiple plugins exist with a similar technology stack (Java + Maven or Gradle), maintaining individual workflows per repository or manually uploading assets scales poorly.

## Goals / Non-Goals

**Goals:**
- Automate the process of compiling the plugin upon a release event.
- Automatically detect the build tool (Maven or Gradle) based on root configuration files (`pom.xml` vs `build.gradle`/`build.gradle.kts`).
- Isolate the compiled main JAR from auxiliary JARs (javadoc, sources).
- Upload the JAR to the corresponding GitHub Release.

**Non-Goals:**
- Creating releases automatically (the user creates the release/tag manually, and this workflow attaches the asset).
- Automatic deployments to external systems (like SpigotMC or Modrinth) outside of GitHub Releases.

## Decisions

- **Reusable Workflow vs Composite Action**: Selected Reusable Workflow (`workflow_call`). Rationale: It encapsulates the entire pipeline execution environment (like checking out code, setting up Java, running bash) into a self-contained unit, meaning consumers just need a few lines in their repository to use it without boilerplate.
- **Bash Script for Tool Detection**: Instead of relying on a dedicated GitHub Action for auto-detection, a short bash step inspects the presence of `pom.xml` and `build.gradle*`. This keeps the workflow dependency-light and fast.
- **Exclusion of Javadocs/Sources JARs**: The `find` command will explicitly ignore JARs ending in `-javadoc.jar` and `-sources.jar` to ensure only the main compiled plugin JAR is uploaded.

## Risks / Trade-offs

- **Risk:** Multi-module projects might generate multiple output JARs.
  **Mitigation:** The `find ... | head -n 1` ensures only one artifact is uploaded. For complex multi-module projects, the consumer could specify a `build-tool` other than auto or the logic might need expansion. For now, the assumption holds that plugins generate a single primary artifact in `target/` or `build/libs/`.
