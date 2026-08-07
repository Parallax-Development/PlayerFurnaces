# recipe-import Specification

## Purpose
TBD - created by archiving change import-recipes-command. Update Purpose after archive.
## Requirements
### Requirement: Admin Command `/pfa import`
Command `/pfa import <plugin> [--overwrite|-f]` MUST require `playerfurnaces.admin` permission. If the plugin parameter is missing or invalid, an informative usage message MUST be returned to the sender. Tab completion MUST present only available and supported plugins (e.g. `craftorithm`) that are currently enabled on the server.

#### Scenario: Admin runs import command
- **Given** an admin with `playerfurnaces.admin` permission
- **When** executing `/pfa import craftorithm`
- **Then** the plugin imports Craftorithm smelting recipes into `recipes/craftorithm/`

### Requirement: Recipe Import Execution
The import system MUST extract smelting/furnace recipes from the specified target plugin. Each imported recipe MUST be saved in `plugins/PlayerFurnaces/recipes/<plugin>/<recipe_id>.yml`. If an output YAML file already exists and `--overwrite` (or `-f`) is NOT specified, the file MUST NOT be overwritten, and the importer MUST report skipped count. If `--overwrite` (or `-f`) IS specified, existing files MUST be overwritten with freshly generated recipe YAML configurations.

#### Scenario: Recipe file collision
- **Given** an existing file `recipes/craftorithm/ruby_smelt.yml`
- **When** running `/pfa import craftorithm` without `--overwrite`
- **Then** the existing file is skipped and counted as skipped

### Requirement: Craftorithm Integration
The importer MUST dynamically query Craftorithm via reflection/API without causing compile errors or crashes if Craftorithm is absent or disabled. The generated YAML recipe MUST map input items, output items, `cook-time-ticks`, and `experience` into standard PlayerFurnaces format using the primary namespace `craftorithm` while supporting `crafthorim` as an alias. Input definitions MUST correctly handle both `MaterialChoice` and `ExactChoice`, preserving display names, lore, custom model data, and custom PDC tags.

#### Scenario: Craftorithm plugin is enabled
- **Given** Craftorithm plugin is active on the server
- **When** importing recipes
- **Then** Craftorithm recipes are correctly converted to PlayerFurnaces YAML format

#### Scenario: Importing Craftorithm recipe with MaterialChoice and item metadata
- **Given** a Craftorithm smelting recipe with custom lore and custom model data
- **When** executing `/pfa import craftorithm`
- **Then** the generated YAML contains correct `craftorithm:<id>` item IDs, display names, lore lines, custom model data, and PDC entries that validate cleanly in `RecipeManager`

### Requirement: Immediate Hot Reload
After completing recipe import operations, the plugin MUST automatically invoke `RecipeManager.loadRecipes()`. The command sender MUST receive a summary message stating how many recipes were imported and skipped.

#### Scenario: Post import reload
- **Given** a successful recipe import operation
- **When** the import process finishes
- **Then** `RecipeManager.loadRecipes()` is automatically called and summary message is sent

