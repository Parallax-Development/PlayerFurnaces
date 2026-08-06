# virtual-furnaces Specification Delta

## MODIFIED Requirements

### Requirement: Virtual Furnace Selector GUI
The system SHALL display an interactive GUI when a player executes `/furnace` or `/horno` showing all virtual furnaces available to that player, utilizing the dynamic layout engine to render the visual structure. Access permissions for virtual furnaces #1 and #2 in `plugin.yml` SHALL default to `op` instead of `true`.

#### Scenario: Opening furnace selector
- **WHEN** player executes `/furnace`
- **THEN** system opens a GUI listing furnaces with visual status indicators (Smelting, Idle, Out of Fuel, Locked) mapped to the dynamic layout slots based on player permissions `playerfurnaces.furnace.<number>`

#### Scenario: Opening locked furnace
- **WHEN** player clicks on a locked furnace icon in the dynamically rendered GUI
- **THEN** system prevents access and sends a message explaining that permission `playerfurnaces.furnace.<number>` is required
