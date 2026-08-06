# gui-item-customization Specification

## Purpose
TBD - created by archiving change fix-force-open-perms-hex-heads. Update Purpose after archive.
## Requirements
### Requirement: CustomModelData and Player Head Support in Menus
The system SHALL support `custom_model_data`, `skull_owner`, and `skull_texture` configuration properties for GUI menu items defined in `menus.yml`.

#### Scenario: Rendering CustomModelData on GUI item
- **WHEN** a slot in `menus.yml` defines `custom_model_data: 1005`
- **THEN** the rendered ItemStack has its ItemMeta `customModelData` set to `1005`.

#### Scenario: Rendering Player Head by owner placeholder
- **WHEN** a slot in `menus.yml` specifies `material: PLAYER_HEAD` and `skull_owner: "{player}"`
- **THEN** the rendered ItemStack displays the target player's head skin profile.

