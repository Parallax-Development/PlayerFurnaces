# Capability: Recipe Import

## MODIFIED Requirements

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
