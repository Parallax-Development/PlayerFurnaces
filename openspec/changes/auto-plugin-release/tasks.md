## 1. Setup GitHub Actions Directory

- [x] 1.1 Create `.github/workflows` directory if it does not exist.
- [x] 1.2 Create `reusable-release.yml` file in the workflows directory.

## 2. Core Workflow Implementation

- [x] 2.1 Define the `workflow_call` trigger and its inputs (`java-version`, `build-tool`).
- [x] 2.2 Add `actions/checkout` and `actions/setup-java` steps.
- [x] 2.3 Implement the bash script step for build tool auto-detection and compilation.
- [x] 2.4 Add JAR isolation logic to exclude sources and javadoc jars.
- [x] 2.5 Add the final step to upload the JAR to GitHub Releases using `gh release upload`.

## 3. Verification

- [x] 3.1 Review `reusable-release.yml` syntax.
- [x] 3.2 Ensure the workflow correctly matches the specs and design (outputs `jar_path` and uploads it).
