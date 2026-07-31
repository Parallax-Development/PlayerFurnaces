## Why

Minecraft players currently rely on placing physical furnace blocks in the world, which takes up spatial footprint, suffers from chunk loading limitations, and lacks permission-based access control. Inspired by the virtual vault management paradigm of AxVaults, PlayerFurnaces provides players with virtual, personal, GUI-accessible furnaces that cook items asynchronously and persist securely across server restarts.

## What Changes

- **Virtual Furnace Hub (`/furnace`)**: Interactive GUI displaying all personal furnaces a player has access to, with real-time status indicators (Smelting, Idle, Out of Fuel, Locked), custom names, and custom icons.
- **Virtual Smelter Interface**: Dedicated GUI mimicking vanilla furnace mechanics (Input, Fuel, Output slots) with offline smelting calculation via timestamp deltas.
- **Permission-Based Access**: Access limits controlled by permission nodes (`playerfurnaces.furnace.<number>`).
- **Data Persistence & Anti-Dupe**: Database storage (H2 / SQLite) using binary serialization for safe preservation of custom item NBT and PDC data.
- **Admin Management Commands (`/pfadmin`)**: Commands for server administrators to inspect and manage any player's online or offline furnaces (`/pfadmin view <player> <id>`), reload configurations, and clear furnaces.

## Capabilities

### New Capabilities
- `virtual-furnaces`: Core virtual furnace system including GUI selector, furnace smelting interface, permission control, offline smelting calculations, and admin inspection tools.

### Modified Capabilities
*(None - fresh plugin specification)*

## Impact

- **Storage**: SQLite / H2 local database storage file in plugin data directory.
- **Commands**: Registration of `/furnace` (or `/horno`), `/furnaces`, and `/pfadmin` / `/playerfurnacesadmin`.
- **Tech Stack & Dependencies**: PaperMC 1.20+ API, Java 21, Gradle (Kotlin DSL `build.gradle.kts`).

