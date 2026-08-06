## Context

In `PlayerFurnaces`, color parsing is handled by `ColorUtils.colorize(String text)`. Currently, `HEX_PATTERN` uses `Pattern.compile("&#([A-Fa-f0-9]{6})")`, which requires an ampersand (`&`). Users configuring `#FB5607` directly in `menus.yml` or recipe files get raw, unparsed color codes. In addition, `ChatColor.translateAlternateColorCodes('&', processed)` is run before calling `MINI_MESSAGE.deserialize(processed)`. Because MiniMessage throws an exception when encountering section symbols (`§`), any text combining `&` legacy codes with hex tags triggers an uncaught exception in MiniMessage that defaults back to returning unparsed text.

Lastly, `FurnaceViewGui` and `FurnaceHubGui` replace placeholders (`{id}`, `{time}`, `{amount}`, `{item}`, etc.) on `stateData.getName()` and `stateData.getLore()`, but do not pass the finalized string back through `ColorUtils.colorize()`.

## Goals / Non-Goals

**Goals:**
- Update `ColorUtils.colorize()` to support direct `#RRGGBB` format, `&#RRGGBB` format, and MiniMessage `<#RRGGBB>` tags.
- Safely handle legacy `&` formatting without causing MiniMessage deserialization failures.
- Ensure all GUI item display names and lores in `FurnaceViewGui` and `FurnaceHubGui` are colorized after placeholder replacement.

**Non-Goals:**
- Changing existing recipe or message configuration schema structures.

## Decisions

1. **Flexible HEX Regex Matching**:
   Update `HEX_PATTERN` in `ColorUtils` to match `(?:&#|#|(?<!<)#)([A-Fa-f0-9]{6})` or convert both `&#RRGGBB` and `#RRGGBB` to standard Minecraft section hex code `§x§R§R§G§G§B§B` or MiniMessage `<#RRGGBB>` tags before processing.
   
2. **Safe Color Processing Sequence**:
   - Convert `#RRGGBB` and `&#RRGGBB` into `<#RRGGBB>` tags if MiniMessage formatting is present, OR convert `#RRGGBB` and `&#RRGGBB` directly to legacy section hex format `§x§R§R§G§G§B§B`.
   - If MiniMessage tags (`<` and `>`) are detected, parse with MiniMessage *before* applying `ChatColor.translateAlternateColorCodes('&', ...)`.
   - Apply `ChatColor.translateAlternateColorCodes('&', ...)` after MiniMessage serialization so `&` codes do not crash `MINI_MESSAGE.deserialize()`.

3. **GUI Item Post-Processing**:
   In `FurnaceViewGui.createItem()` and `FurnaceHubGui.createItem()`, pass `meta.setDisplayName(ColorUtils.colorize(name))` and `lore.add(ColorUtils.colorize(line))` after replacement of dynamic placeholders.

## Risks / Trade-offs

- **[Risk]** Matching raw `#RRGGBB` might accidentally match non-color strings if they happen to look like 6 hex digits after `#`.
  - **Mitigation:** Only match standard 6-digit hex string boundaries `#([A-Fa-f0-9]{6})`.

- **[Risk]** Double colorizing performance impact in GUI refresh.
  - **Mitigation:** `ColorUtils.colorize` is lightweight regex and string manipulation; impact is negligible for typical 54-slot inventories.
