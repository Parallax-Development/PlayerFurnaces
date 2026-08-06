## MODIFIED Requirements

### Requirement: MiniMessage and HEX Color Support
The system SHALL parse HEX color codes (`#RRGGBB`, `&#RRGGBB`, and `<#RRGGBB>`) and Kyori MiniMessage tags (`<red>`, `<gradient>`, etc.) across all chat messages, GUI inventory titles, item display names, and item lore lines, including after placeholder substitution in GUI menus.

#### Scenario: Translating HEX colors and ampersand codes in text
- **WHEN** text containing `#ff0000Header`, `&#ff0000Header`, `<#ff0000>Header`, or `&aSub` is colorized
- **THEN** the system outputs formatted text with exact hex colors and legacy formatting applied.

#### Scenario: Translating HEX colors post placeholder replacement in menus
- **WHEN** item display names or lore lines containing `#FB5607SIN COMBUSTIBLE` or placeholders with color tags are rendered in a furnace GUI
- **THEN** the GUI item meta displays full 24-bit RGB color without raw `#RRGGBB` color codes visible.

#### Scenario: MiniMessage gradients in GUI title
- **WHEN** a GUI title uses `<gradient:#ff0000:#00ff00>Virtual Furnaces</gradient>`
- **THEN** the inventory opens with a color gradient title.
