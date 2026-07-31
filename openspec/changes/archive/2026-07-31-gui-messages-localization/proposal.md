## Why

GUI titles, item display names, item lores, status indicators, and action button labels are currently hardcoded in Spanish across GUI classes (`FurnaceHubGui`, `FurnaceViewGui`, `GuiListener`). To provide full localization support with English as the default primary language, all GUI texts need to be moved to `messages.yml` and managed dynamically by `MessageManager`.

## What Changes

- Update default `src/main/resources/messages.yml` to English as the primary language and add configuration sections for GUI Hub, Furnace View GUI, and item collection messages.
- Extend `MessageManager` with helper methods for retrieving lists of lore lines, item titles, and status placeholders.
- Refactor `FurnaceHubGui.java` to fetch all item names, status indicators, and lore lines from `MessageManager`.
- Refactor `FurnaceViewGui.java` to fetch progress indicators, fuel status items, collect button, and back button text from `MessageManager`.
- Refactor `GuiListener.java` to use `MessageManager` for collection feedback messages.

## Capabilities

### New Capabilities
- `gui-localization`: Configurable GUI item names, lore lines, titles, and feedback messages externalized into `messages.yml` with English defaults.

### Modified Capabilities

## Impact

- **Affected Code**: `messages.yml`, `MessageManager.java`, `FurnaceHubGui.java`, `FurnaceViewGui.java`, `GuiListener.java`.
- **User Impact**: Server admins can fully translate and customize GUI elements and messages in `messages.yml`.
