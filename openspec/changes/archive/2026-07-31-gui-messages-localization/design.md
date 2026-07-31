## Context

GUI components (`FurnaceHubGui`, `FurnaceViewGui`) and event listeners (`GuiListener`) contain hardcoded Spanish strings and lore entries. The user requested fully configurable GUI translation keys in `messages.yml` with English as the primary default language.

## Goals / Non-Goals

**Goals:**
- Add `gui.` and `collection.` sections to `messages.yml` in English.
- Extend `MessageManager` to support list retrieval with placeholder replacements (`getMessageList(String key, String... replacements)`).
- Update `FurnaceHubGui`, `FurnaceViewGui`, and `GuiListener` to retrieve all names, lores, titles, and feedback from `MessageManager`.

**Non-Goals:**
- Creating separate per-player locale files (multi-language plugin files will remain in single `messages.yml` for server-wide localization).

## Decisions

- **Decision: `getMessageList` in `MessageManager`**
  - *Rationale*: Allows easy formatting of multi-line lores in GUIs while performing color translation (`&`) and placeholder substitution per line.

## Risks / Trade-offs

- **[Risk]** Missing keys in custom `messages.yml` files when upgrading.
  - *Mitigation*: Provide default fallbacks in `MessageManager` getter methods so GUI rendering never throws NullPointerException or displays blank items.
