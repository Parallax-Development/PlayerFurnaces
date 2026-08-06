## ADDED Requirements

### Requirement: MiniMessage and HEX Color Support
The system SHALL parse HEX color codes (`&#RRGGBB` and `<#RRGGBB>`) and Kyori MiniMessage tags (`<red>`, `<gradient>`, etc.) across all chat messages, GUI inventory titles, item display names, and item lore lines.

#### Scenario: Translating HEX colors and ampersand codes in text
- **WHEN** text containing `&#ff0000Header` or `<#ff0000>Header` or `&aSub` is colorized
- **THEN** the system outputs formatted text with exact hex colors and legacy formatting applied.

#### Scenario: MiniMessage gradients in GUI title
- **WHEN** a GUI title uses `<gradient:#ff0000:#00ff00>Virtual Furnaces</gradient>`
- **THEN** the inventory opens with a color gradient title.
