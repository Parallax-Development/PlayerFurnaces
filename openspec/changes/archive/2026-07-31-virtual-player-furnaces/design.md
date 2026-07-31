## Context

PlayerFurnaces is a Bukkit/Paper plugin designed to provide virtual, personal, GUI-accessible furnaces to Minecraft players. It addresses space limitations, chunk loading issues, and item dupe vulnerabilities by offering centralized, database-backed furnace management inspired by AxVaults.

## Goals / Non-Goals

**Goals:**
- Provide a responsive GUI menu selector (`/furnace`) to view and manage personal virtual furnaces.
- Implement vanilla-accurate furnace smelting mechanics operating offline via timestamp delta calculations using PaperMC 1.20+ API.
- Enforce permission-based access control (`playerfurnaces.furnace.<number>`).
- Persist furnace data, inputs, fuel, and outputs into SQLite/H2 with anti-dupe binary item serialization.
- Provide administrative inspection commands (`/pfadmin view <player> <id>`).
- Build project using **Java 21** and **Gradle (Kotlin DSL - `build.gradle.kts`)**.


**Non-Goals:**
- V1 does NOT include RPG mechanics, level systems, furnace speed upgrades, fortune/duplication upgrades, or fuel efficiency multipliers.
- V1 does NOT require physical furnace block bindings in the Minecraft world (all virtual via GUI).

## Decisions

### 1. Offline Smelting via Timestamp Delta vs. Active Ticking Tasks
- **Decision**: Calculate furnace state lazily upon GUI open or background processing using timestamp deltas (`currentTime - lastBurnTime`), rather than running thousands of active BukkitRunnables per furnace tick.
- **Rationale**: Minimal performance footprint on server TPS regardless of the total number of registered furnaces.

### 2. Binary Serialization for Item Storage (SQLite / H2)
- **Decision**: Use Bukkit's `ItemStack.serializeAsBytes()` / `ConfigurationSerialization` stored in SQLite/H2 BLOB columns.
- **Rationale**: Completely dupe-proof and preserves all custom NBT tags, PersistentDataContainer (PDC) attributes, and custom item metadata (ItemsAdder, Oraxen, MMOItems).

### 3. Permission-Based Slot Resolution
- **Decision**: Resolve furnace access using Vault Permissions or Bukkit permission checks for `playerfurnaces.furnace.<N>`.
- **Rationale**: Direct alignment with AxVaults permission model (`axvaults.vault.<N>`), making setup familiar to server administrators.

## Risks / Trade-offs

- **[Risk] Vanilla Recipe Compatibility** → Mitigation: Use Bukkit's `FurnaceRecipe` registry lookup to ensure all vanilla ores, foods, and custom server smelting recipes are seamlessly supported.
- **[Risk] Container Dupe Scenarios on Server Crashes** → Mitigation: Execute atomic database transactions on item insertion and removal from furnace slots.
