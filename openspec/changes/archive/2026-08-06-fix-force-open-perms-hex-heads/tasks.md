## 1. Fix Admin Force-Open Permissions

- [x] 1.1 Update `AdminCommand.java` to check `plugin.getFurnaceManager().hasPermissionForFurnace(target, furnaceId)` instead of hardcoded `playerfurnaces.use.<id>`.
- [x] 1.2 Verify `/pfadmin force-open` works for default players having `playerfurnaces.furnace.1` permission node.

## 2. Text Colorization Utility (HEX & MiniMessage)

- [x] 2.1 Create `ColorUtils.java` utility supporting Kyori MiniMessage, HEX codes (`&#RRGGBB` and `<#RRGGBB>`), and ampersand color codes.
- [x] 2.2 Refactor `MessageManager.java` and `MenuManager.java` to use `ColorUtils.format()` for all messages, titles, item names, and lore lines.

## 3. CustomModelData & Player Head Support in GUIs

- [x] 3.1 Expand `MenuStateData.java` to include `customModelData` (Integer), `skullOwner` (String), and `skullTexture` (String).
- [x] 3.2 Update `MenuManager.java` YAML parser to parse `custom_model_data`, `skull_owner`, and `skull_texture` from `menus.yml`.
- [x] 3.3 Update item creation in `FurnaceHubGui.java` and `FurnaceViewGui.java` to apply `CustomModelData` and `SkullMeta` (with `{player}` placeholder replacement).

## 4. Verification & Testing

- [x] 4.1 Run unit tests and Gradle build (`./gradlew build`) to ensure clean compilation and zero regressions.
