# 📚 PlayerFurnaces - Wiki & User Manual

Welcome to the official **PlayerFurnaces** Wiki!

This documentation contains detailed guides, configuration file syntax breakdowns, custom recipe and fuel tutorials, as well as integration manuals for third-party item plugins.

---

## 🗺️ Table of Contents

### 1. ⚙️ [General Configuration (`config.yml` & `messages.yml`)](file:///c:/Users/antua/OneDrive/Documentos/Programming/JAVA/PLUGINS/PlayerFurnaces/docs/Configuration.md)
Learn how to configure the database engine, per-player furnace limits, GUI refresh rates, and message formatting (supports legacy `&` codes, Hex, and MiniMessage).

### 2. 🧪 [Custom Recipe System (`recipes/*.yml`)](file:///c:/Users/antua/OneDrive/Documentos/Programming/JAVA/PLUGINS/PlayerFurnaces/docs/Custom-Recipes.md)
Complete manual on defining modular smelting recipes using YAML files. Detailed field breakdowns: `input`, `result`, `cook-time-ticks`, `experience`, `custom-model-data`, `pdc`, and `fuel`.

### 3. 🔥 [Custom Fuel System (`fuels/*.yml`)](file:///c:/Users/antua/OneDrive/Documentos/Programming/JAVA/PLUGINS/PlayerFurnaces/docs/Custom-Fuels.md)
Guide to defining global custom fuels with custom burn durations (`burn-time-ticks`), linked to vanilla materials or external plugin items.

### 4. 🧩 [External Plugin Integration & ItemProvider API](file:///c:/Users/antua/OneDrive/Documentos/Programming/JAVA/PLUGINS/PlayerFurnaces/docs/External-Item-Providers.md)
Explanation of namespaced item resolution (`namespace:item_id`), native **Craftorithm** integration, PDC matching, and developer guide for registering new `ItemProvider` instances.

### 5. 🔑 [Commands & Permissions](file:///c:/Users/antua/OneDrive/Documentos/Programming/JAVA/PLUGINS/PlayerFurnaces/docs/Commands-and-Permissions.md)
Exhaustive list of player and administrative commands, aliases, usage examples, and permission tree structure (`playerfurnaces.furnace.<id>`).

### 6. 💾 [Persistence & Databases (SQLite / H2)](file:///c:/Users/antua/OneDrive/Documentos/Programming/JAVA/PLUGINS/PlayerFurnaces/docs/Database-and-Storage.md)
Details on binary item storage powered by HikariCP, differences between SQLite and H2, and anti-duplication metadata guarantees.

---

## ❓ Frequently Asked Questions (FAQ)

### How do I grant more furnaces to a player or VIP rank?
Simply assign the corresponding permission node. For example, to grant access to furnace #3 for a VIP group in LuckPerms:
```bash
/lp group vip permission set playerfurnaces.furnace.3 true
```

### What happens if a player disconnects while items are smelting?
PlayerFurnaces uses **delta-time offline smelting calculations**. When the player opens their furnace again or reconnects, the plugin calculates the elapsed time in milliseconds and processes exactly all items and fuel that would have been consumed during their absence.

### Will custom NBT/PDC item data be lost?
No. All items placed in virtual furnaces are serialized directly into binary byte arrays using Bukkit's native APIs, keeping all CustomModelData, lore, names, enchantments, and PDC tags from plugins like Craftorithm, Oraxen, MMOItems, or ItemsAdder completely intact.
