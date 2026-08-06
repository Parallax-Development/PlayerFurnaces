## Why

Players using group DEFAULT or basic furnace permissions are currently blocked when an admin runs `/pfadmin force-open <player> <index>` because `AdminCommand` checks an invalid permission node (`playerfurnaces.use.<id>`) instead of standard furnace permissions. Additionally, `PlayerFurnaces` lacks modern HEX color and MiniMessage syntax support, as well as `CustomModelData` and Player Head (Skull) customization for GUI menu icons.

## What Changes

- Fix `/pfadmin force-open` command to check `playerfurnaces.furnace.<id>` (via `FurnaceManager#hasPermissionForFurnace`) instead of the unmapped `playerfurnaces.use.<id>`.
- Add HEX color code (`&#RRGGBB` / `<#RRGGBB>`) and Kyori MiniMessage formatting support across all messages, GUI titles, item names, and lore lines.
- Add support for `custom_model_data` and player heads (`skull_owner` / `skull_texture`) in `menus.yml` and GUI item renderer (`FurnaceHubGui`, `FurnaceViewGui`, `MenuManager`).

## Capabilities

### New Capabilities
- `hex-color-formatting`: Support for HEX color codes and MiniMessage formatting across chat messages and GUI item meta.
- `gui-item-customization`: Support for CustomModelData and Player Head textures/owners in GUI item state configurations.

### Modified Capabilities
- `virtual-furnaces`: Standardize furnace permission checks for `force-open` admin commands.

## Impact

- `AdminCommand.java`: Update permission validation for `force-open`.
- `MessageManager.java` and `MenuManager.java`: Integrate MiniMessage and HEX color parsing.
- `MenuStateData.java` and GUI classes: Support `customModelData` and `skullOwner`/`skullTexture` configuration properties.
- `menus.yml`: Update YAML schema to allow `custom_model_data`, `skull_owner`, and `skull_texture`.
