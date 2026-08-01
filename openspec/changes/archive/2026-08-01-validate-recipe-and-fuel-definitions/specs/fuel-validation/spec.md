## ADDED Requirements

### Requirement: Fuel Material and Item Provider Validation
The system SHALL validate fuel type identifiers (including Bukkit materials and third-party item provider IDs) during fuel configuration file loading, logging descriptive warnings for invalid entries.

#### Scenario: Fuel configuration with invalid material or type
- **WHEN** a fuel YAML file contains an unrecognized material or item provider ID in its `type` field (e.g., `COAAL`)
- **THEN** the system logs a `WARNING` specifying the file name, fuel key, and invalid type identifier, and skips registering that fuel entry.

#### Scenario: Summary of fuel loading
- **WHEN** custom fuels finish loading during plugin startup or reload
- **THEN** the system logs the count of successfully registered fuels as well as the count of fuels skipped due to configuration errors.
