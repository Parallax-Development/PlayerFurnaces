## 1. Project Setup & Database Layer

- [x] 1.1 Setup Gradle build configuration with Kotlin DSL (`build.gradle.kts`), Java 21 target, PaperMC 1.20+ API dependency, and H2/SQLite database drivers
- [x] 1.2 Implement Database Manager for furnace state persistence and binary item BLOB serialization


- [x] 1.3 Create data models for VirtualFurnace and FurnaceItem



## 2. Core Smelting Engine & Offline Logic

- [x] 2.1 Implement vanilla furnace recipe lookup and fuel burn calculation manager
- [x] 2.2 Implement timestamp delta engine for background and offline smelting state progression



## 3. User Interfaces (GUI)

- [x] 3.1 Build Furnace Hub Selector GUI (`/furnace`) with status icons (Smelting, Idle, Out of Fuel, Locked)
- [x] 3.2 Build Virtual Furnace GUI with Input, Fuel, and Output slots, including "Collect All" button



## 4. Commands, Permissions & Admin Tools

- [x] 4.1 Implement player commands `/furnace` and `/furnace [id]` with permission node checking (`playerfurnaces.furnace.<N>`)
- [x] 4.2 Implement admin commands `/pfadmin view <player> <id>` and `/pfadmin reload`



## 5. Verification & Testing

- [x] 5.1 Test NBT/PDC item preservation across furnace operations and server reloads
- [x] 5.2 Verify offline smelting progress calculations and anti-dupe safety

