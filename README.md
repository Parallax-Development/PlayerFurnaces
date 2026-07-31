# ━━━━━━━ 🔥 PlayerFurnaces 🔥 ━━━━━━━

> **Advanced Virtual & Seamless Furnace System for Minecraft Servers (Paper/Spigot 1.20+)**
> *Inspired by premium virtual storage systems (like AxVaults), bringing item smelting to the next level.*

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.20%2B-brightgreen.svg)](https://papermc.io)
[![Java Version](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://adoptium.net)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Database](https://img.shields.io/badge/Database-SQLite%20%7C%20H2-yellow.svg)](https://github.com)

---

## 📖 Table of Contents
- [Showcase & Applications](#-showcase--applications)
- [Key Features](#-key-features)
- [Architecture & Technical Details](#-architecture--technical-details)
- [Commands & Permissions](#-commands--permissions)
- [Quick Start](#-quick-start)
- [Documentation & Wiki](#-documentation--wiki)

---

## 🌟 Showcase & Applications

**PlayerFurnaces** revolutionizes traditional Minecraft smelting mechanics by eliminating the need to place physical furnace blocks in the world. It provides every player with an interactive GUI menu (**Virtual Furnace Hub**) with access to multiple independent personal virtual furnaces, protected by permissions and featuring real-time offline processing.

```
                   ┌──────────────────────────────────────┐
                   │    PLAYER COMMAND: /furnace          │
                   └──────────────────┬───────────────────┘
                                      │
                                      ▼
    ┌──────────────────────────────────────────────────────────────────┐
    │                    VIRTUAL FURNACE HUB                           │
    │  [Furnace #1] [Furnace #2] [Furnace #3] ... [Furnace #14] (Up 54)│
    └─────────────────────────────────┬────────────────────────────────┘
                                      │ (Click Furnace)
                                      ▼
    ┌──────────────────────────────────────────────────────────────────┐
    │                   VIRTUAL FURNACE INTERFACE                      │
    │  [ Inputs ] ➔ ⚡ [ Progress % ] ➔ [ Processed Outputs ]        │
    │  [ Active Fuel (s) ]             [ 📦 Collect All ]              |
    └──────────────────────────────────────────────────────────────────┘
```

### 🎯 Server Applications
* **Survival / RPG / Towny**: Eliminates lag caused by thousands of physical furnace blocks ticking and interacting in the world.
* **Skyblock / OneBlock**: Saves valuable island space and prevents item theft in shared areas.
* **Advanced Economies & MMOs**: Allows creating custom recipes for custom ores (e.g., Craftorithm, Oraxen, ItemsAdder) with custom cooking durations and fuels.
* **VIP Rewards / Ranks**: Grants additional virtual furnaces progressively via permission nodes (`playerfurnaces.furnace.<id>`).

---

## ✨ Key Features

* 🟢 **Interactive GUI Hub**: Visually displays the real-time status of each furnace:
  * **Smelting** (`BLAST_FURNACE`): Displays processed item, quantity, and remaining fuel seconds.
  * **No Fuel** (`FURNACE`): Indicates fuel is required to continue.
  * **Idle** (`FURNACE`): Ready for input items.
  * **Locked** (`RED_STAINED_GLASS_PANE`): Displays the required permission node to unlock.
* ⚡ **Intelligent Offline Smelting**: Furnaces automatically calculate elapsed time (delta-time) upon reopening the menu or reconnecting, ensuring production never stalls.
* 📦 **Anti-Dupe Item Persistence (Intact NBT/PDC)**: Binary database storage (**SQLite** or **H2**) powered by HikariCP. Preserves Custom Model Data, lore, custom names, Hex/MiniMessage colors, and PDC data without item duplication risks.
* 🧪 **Modular Custom Recipes (`recipes/*.yml`)**:
  * Override vanilla recipes or create custom ones.
  * Define input/output items, cooking time (`cook-time-ticks`), experience payout, and recipe-specific fuel constraints.
* 🔥 **Custom Fuel Definitions (`fuels/*.yml`)**:
  * Register global custom fuels with custom burn durations (`burn-time-ticks`).
* 🔌 **Extensible Item Provider Integration (ItemProvider API)**:
  * Native & reflection hook support for **Craftorithm** (`crafthorim:item_id`).
  * Fallback item matching using PDC (*PersistentDataContainer*) tags.
* 🛡️ **Admin Tools (`/pfadmin`)**:
  * Real-time inspection of any player's hub or individual furnace (online or offline) via `/pfadmin view <player> [id]`.
  * Hot reload of configurations and recipes via `/pfadmin reload`.

---

## 🛠️ Architecture & Technical Details

The plugin is designed with a modular, reactive architecture:

```
  ┌──────────────────┐      ┌────────────────────┐      ┌──────────────────────┐
  │ Player / Admin   │ ───► │  GuiListener &     │ ───► │   FurnaceEngine      │
  │ Commands         │      │  FurnaceViewGui    │      │  (Delta-Time Smelt)  │
  └──────────────────┘      └────────────────────┘      └──────────┬───────────┘
                                                                   │
                                                                   ▼
  ┌──────────────────┐      ┌────────────────────┐      ┌──────────────────────┐
  │ ItemProvider     │ ◄─── │  RecipeManager &   │ ◄─── │  DatabaseManager     │
  │ Registry         │      │  FuelManager       │      │  (HikariCP/SQLite/H2)│
  └──────────────────┘      └────────────────────┘      └──────────────────────┘
```

1. **`FurnaceEngine`**: Core smelting engine. Independent of physical block ticks in the world. Uses timestamps (`lastUpdatedTimestamp`) to compute mathematically exact elapsed smelting cycles.
2. **`ItemResolverRegistry`**: Extensible `ItemProvider` registry resolving namespaced identifiers (`namespace:item_id`, e.g., `crafthorim:ruby_ingot`) or vanilla/local items.
3. **`DatabaseManager`**: Asynchronous data layer using HikariCP to serialize Bukkit item stacks to binary byte arrays for maximum NBT/PDC fidelity.

---

## 📜 Commands & Permissions

### 🎮 Player Commands
| Command | Aliases | Permission | Description |
| :--- | :--- | :--- | :--- |
| `/furnace` | `/furnaces`, `/horno`, `/hornos`, `/pf` | `playerfurnaces.command.use` | Opens the main virtual furnace hub. |
| `/furnace <id>` | - | `playerfurnaces.furnace.<id>` | Directly opens the specified virtual furnace. |

### 👮 Admin Commands
| Command | Permission | Description |
| :--- | :--- | :--- |
| `/pfadmin view <player> [id]` | `playerfurnaces.admin` | Inspects a player's hub or specific virtual furnace. |
| `/pfadmin reload` | `playerfurnaces.admin` | Reloads `config.yml`, `messages.yml`, recipes, and fuels. |

### 🔑 Permission Nodes
* `playerfurnaces.command.use` (Default: `true`): Grants access to the base `/furnace` command.
* `playerfurnaces.admin` (Default: `op`): Access to `/pfadmin` commands.
* `playerfurnaces.furnace.<1-54>` (Default for 1 & 2: `true`): Unlocks virtual furnace number `<id>`.

---

## 🚀 Quick Start

1. Download the compiled **PlayerFurnaces** `.jar` file.
2. Place the `.jar` inside your Paper / Spigot server's `plugins/` directory (1.20+).
3. Start the server to generate initial configuration files and databases.
4. *(Optional)* Customize `plugins/PlayerFurnaces/config.yml`, `recipes/`, and `fuels/` as needed.
5. Run `/pfadmin reload` to apply changes live.

---

## 📚 Documentation & Wiki

For detailed guides, syntax breakdowns, and integration manuals, visit our official **Wiki** in the [`docs/`](docs) directory:

* 📘 [Wiki Index](docs/README.md)
* ⚙️ [Configuration Manual (`config.yml` & `messages.yml`)](docs/Configuration.md)
* 🧪 [Custom Recipes Guide (`recipes/*.yml`)](docs/Custom-Recipes.md)
* 🔥 [Custom Fuels Guide (`fuels/*.yml`)](docs/Custom-Fuels.md)
* 🧩 [External Plugin Integration & ItemProvider API](docs/External-Item-Providers.md)
* 🔑 [Commands & Permissions Reference](docs/Commands-and-Permissions.md)
* 💾 [Database & Storage (SQLite / H2)](docs/Database-and-Storage.md)

---
*Built with ❤️ for high-performance Minecraft communities.*
