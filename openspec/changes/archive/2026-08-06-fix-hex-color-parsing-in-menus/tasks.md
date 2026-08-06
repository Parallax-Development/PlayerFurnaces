## 1. Color Parser Fixes

- [x] 1.1 Update `HEX_PATTERN` in `ColorUtils.java` to support `#RRGGBB` alongside `&#RRGGBB` and `<#RRGGBB>`.
- [x] 1.2 Reorder color parsing in `ColorUtils.colorize()` to execute MiniMessage deserialization before legacy `&` ampersand translation to avoid `§` section symbol deserialization errors.

## 2. Menu GUI Post-Processing

- [x] 2.1 Update `FurnaceViewGui.java` item creation to run `ColorUtils.colorize()` on display names and lore after placeholder replacement.
- [x] 2.2 Update `FurnaceHubGui.java` item creation to run `ColorUtils.colorize()` on display names and lore after placeholder replacement.

## 3. Verification & Testing

- [x] 3.1 Create unit test in `ColorUtilsTest.java` verifying `#FB5607`, `&#FB5607`, `<#FB5607>`, and mixed legacy/HEX strings.
- [x] 3.2 Run Gradle build (`./gradlew test shadowJar`) to verify all tests pass and binary builds cleanly.
