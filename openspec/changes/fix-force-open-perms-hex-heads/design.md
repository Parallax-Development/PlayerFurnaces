## Context

`PlayerFurnaces` currently has three core limitations:
1. `AdminCommand` checks `playerfurnaces.use.<id>` instead of using `FurnaceManager#hasPermissionForFurnace`, causing permission failures when force-opening furnace 1 for players with default permissions.
2. Messages and GUI items rely solely on legacy Bukkit ampersand color codes (`ChatColor.translateAlternateColorCodes`), missing HEX (`&#RRGGBB` / `<#RRGGBB>`) and Kyori MiniMessage support (`<red>`, `<gradient>`).
3. GUI item slots in `menus.yml` cannot specify `custom_model_data` or player head skull textures (`skull_owner` / `skull_texture`).

## Goals / Non-Goals

**Goals:**
- Centralize permission checking in `AdminCommand.java` to call `plugin.getFurnaceManager().hasPermissionForFurnace(target, furnaceId)`.
- Implement a unified text colorizing utility (`ColorUtils`) supporting MiniMessage, HEX codes (`&#RRGGBB` and `<#RRGGBB>`), and legacy `&` codes.
- Expand `MenuStateData`, `MenuManager`, `FurnaceHubGui`, and `FurnaceViewGui` to support `custom_model_data`, `skull_owner`, and `skull_texture`.

**Non-Goals:**
- Modifying recipe fuel processing or database schema.

## Decisions

### Decision 1: Permission Centralization in `AdminCommand`
- **Choice**: Call `plugin.getFurnaceManager().hasPermissionForFurnace(target, furnaceId)` inside `AdminCommand#onCommand` for `force-open`.
- **Rationale**: `FurnaceManager` handles `playerfurnaces.admin`, `playerfurnaces.furnace.*`, and `playerfurnaces.furnace.<id>` (which defaults to `true` for 1 and 2 in `plugin.yml`).

### Decision 2: Text Colorizer (`ColorUtils`)
- **Choice**: Create `ColorUtils.java` using Paper's `net.kyori.adventure.text.minimessage.MiniMessage` combined with legacy ampersand and Hex regex parsing.
- **Rationale**: Provides full backward compatibility with `&a` while enabling modern `<gradient:#ff0000:#00ff00>` and `&#ff0000` syntax across chat messages, GUI titles, and item Meta.

### Decision 3: CustomModelData and Skull Meta Support
- **Choice**: Add `customModelData` (Integer), `skullOwner` (String), and `skullTexture` (String) fields to `MenuStateData`. Update `MenuManager` YAML parser and item creation in GUIs.
- **Rationale**: Allows server owners to customize GUI icons with custom model textures or player head avatars (supporting `{player}` placeholder).

## Risks / Trade-offs

- **[Risk]** Legacy plugins/servers using Paper 1.20.4 might handle MiniMessage parsing differently if tags are improperly closed.
  - *Mitigation*: Fall back safely to ampersand color code parsing if MiniMessage deserialization encounters unparsed legacy text.
