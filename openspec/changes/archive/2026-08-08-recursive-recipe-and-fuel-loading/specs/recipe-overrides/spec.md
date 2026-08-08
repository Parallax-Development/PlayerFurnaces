## MODIFIED Requirements

### Requirement: Modular YAML Recipe Loading
The system SHALL recursively load custom furnace recipe override files from the `plugins/PlayerFurnaces/recipes/` directory and any of its subdirectories, treating each `.yml` and `.yaml` file as a distinct recipe definition.

#### Scenario: Loading recipe files on startup
- **WHEN** the plugin starts or executes `/furnace reload`
- **THEN** all valid `.yml` and `.yaml` files inside `plugins/PlayerFurnaces/recipes/` and its subdirectories are parsed into custom recipe models and registered in the internal recipe registry.

#### Scenario: Loading recipe files in nested subfolders
- **WHEN** custom recipe YAML files exist inside subdirectories of `plugins/PlayerFurnaces/recipes/` (e.g. `recipes/armors/netherite.yml`)
- **THEN** the system recursively traverses the directory tree and registers all valid recipe files.
