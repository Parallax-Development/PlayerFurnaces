## MODIFIED Requirements

### Requirement: Fuel Material and Item Provider Validation
The system SHALL validate fuel type identifiers (including Bukkit materials and third-party item provider IDs) during fuel configuration file loading across `plugins/PlayerFurnaces/fuels/` and any of its subdirectories, logging descriptive warnings for invalid entries.

#### Scenario: Fuel configuration with invalid material or type
- **WHEN** a fuel YAML file contains an unrecognized material or item provider ID in its `type` field (e.g., `COAAL`)
- **THEN** the system logs a `WARNING` specifying the file name, fuel key, and invalid type identifier, and skips registering that fuel entry.

#### Scenario: Summary of fuel loading
- **WHEN** custom fuels finish loading during plugin startup or reload
- **THEN** the system logs the count of successfully registered fuels from `fuels/` and any subdirectories, as well as the count of fuels skipped due to configuration errors.

#### Scenario: Loading fuel files in nested subfolders
- **WHEN** custom fuel YAML files exist inside subdirectories of `plugins/PlayerFurnaces/fuels/` (e.g. `fuels/custom/magic.yml`)
- **THEN** the system recursively traverses the directory tree and registers all valid fuel files.
