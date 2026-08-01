## ADDED Requirements

### Requirement: Custom Recipe Material and Provider Validation
The system SHALL validate input and result material names and third-party item provider IDs during recipe file loading, logging a descriptive warning and skipping registration for any recipe with invalid material or provider references.

#### Scenario: Recipe with misspelled material name
- **WHEN** a recipe YAML file contains an unrecognized material name in `input` or `result` (e.g., `GLOD_INGOT`)
- **THEN** the system logs a `WARNING` in the server log specifying the file name, recipe ID, section, and invalid material name, and skips registering that recipe.

#### Scenario: Recipe with invalid item provider ID
- **WHEN** a recipe YAML file contains an unrecognized item provider namespace in `id` (e.g., `unknownplugin:item_id`)
- **THEN** the system logs a `WARNING` in the server log specifying the file name, recipe ID, section, and unresolved item provider ID, and skips registering that recipe.

#### Scenario: Summary of recipe loading
- **WHEN** custom recipes finish loading during plugin startup or reload
- **THEN** the system logs the count of successfully registered recipes as well as the count of recipes skipped due to configuration errors.
