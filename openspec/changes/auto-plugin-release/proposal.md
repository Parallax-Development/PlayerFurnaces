## Why

Automating the release process ensures consistent builds across different plugin projects without manual intervention. A universal GitHub Actions workflow standardizes compilation and JAR deployment to GitHub Releases.

## What Changes

- Introduce a reusable GitHub Actions workflow (`workflow_call`).
- Automatically detect the build tool (Maven vs Gradle).
- Compile the plugin using the detected tool.
- Locate the main compiled JAR artifact.
- Upload the JAR to the triggered GitHub Release.

## Capabilities

### New Capabilities
- `auto-plugin-release`: A reusable workflow capable of detecting Maven or Gradle builds and attaching the resulting JAR to GitHub Releases.

### Modified Capabilities
- (None)

## Impact

- Standardizes plugin publication.
- Eliminates manual uploading of `.jar` files to GitHub Releases.
- Allows any new or existing plugin repository to adopt this workflow with a single `uses` reference.
