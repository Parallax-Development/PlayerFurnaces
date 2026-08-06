## Why

HEX color codes written as `#RRGGBB` (without an ampersand `&`) in menu titles, item names, and lore lines are not being parsed by `ColorUtils.colorize()`, causing raw strings like `#FB5607SIN COMBUSTIBLE` to appear in-game tooltips. Additionally, attempting MiniMessage deserialization on strings already converted to legacy section symbols (`§`) causes MiniMessage parsing exceptions, falling back to raw unformatted text.

## What Changes

- Update `ColorUtils.colorize()` to support standard `#RRGGBB` format in addition to `&#RRGGBB`, `{#RRGGBB}`, and MiniMessage `<#RRGGBB>` tags.
- Fix color parsing order in `ColorUtils.colorize()` to ensure legacy `&` formatting codes and MiniMessage tags can co-exist without throwing deserialization exceptions.
- Update `FurnaceViewGui` and `FurnaceHubGui` item creation logic to pass display names and lore through `ColorUtils.colorize()` after replacing dynamic placeholders (`{id}`, `{time}`, `{amount}`, `{item}`, etc.).

## Capabilities

### New Capabilities

*(None)*

### Modified Capabilities

- `hex-color-formatting`: Expand HEX color parsing to support direct `#RRGGBB` strings and ensure error-free MiniMessage/legacy color handling for GUI titles, item names, and lore lines after placeholder replacement.

## Impact

- `dev.darkblade.playerfurnaces.util.ColorUtils`: Updated regex and parsing logic.
- `dev.darkblade.playerfurnaces.gui.FurnaceViewGui`: Colorize item meta display names and lore after placeholder replacement.
- `dev.darkblade.playerfurnaces.gui.FurnaceHubGui`: Colorize item meta display names and lore after placeholder replacement.
- Unit tests: Added test coverage in `ColorUtilsTest` for `#RRGGBB`, `&#RRGGBB`, and placeholder-colorized strings.
