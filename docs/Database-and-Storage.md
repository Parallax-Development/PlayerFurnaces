# 💾 Persistence & Database (SQLite / H2)

This section explains the internal mechanics of **PlayerFurnaces**' storage layer, designed to ensure zero item loss, absolute duplication prevention, and full compatibility with complex NBT/PDC metadata.

---

## 🗄️ Supported Database Engines

PlayerFurnaces uses **HikariCP** as a high-performance database connection pool manager. In `config.yml`, two engines can be configured:

### 1. SQLite (`type: SQLITE`)
* **Recommended for**: Small to medium servers or local development environments.
* **Storage**: Single file inside the plugin's directory (`plugins/PlayerFurnaces/furnaces.db`).
* **Advantages**: Zero external setup, lightweight, and fast access.

### 2. H2 (`type: H2`)
* **Recommended for**: High-traffic servers or server networks.
* **Storage**: Lightweight relational database file compatible with advanced SQL syntax.
* **Advantages**: High transactional performance under heavy concurrent smelting loads.

---

## 📦 Anti-Dupe Item Serialization

To prevent common storage issues (such as losing custom plugin item tags or item duplication exploits):

```
┌─────────────────────────┐
│     org.bukkit.item.    │
│       ItemStack         │
└────────────┬────────────┘
             │ (Bukkit Native NBT Serialization)
             ▼
┌─────────────────────────┐
│       byte[] BLOB       │
└────────────┬────────────┘
             │ (Async HikariCP Persistence)
             ▼
┌─────────────────────────┐
│ SQL Table:              │
│ player_furnaces         │
└─────────────────────────┘
```

1. **Native NBT Serialization**: `ItemStack` objects (input, fuel, and output) are converted to a compressed byte array (`byte[] BLOB`).
2. **Total Attribute Preservation**: Enchantments, CustomModelData, color-formatted display names, lores, durability, and PDC (*PersistentDataContainer*) tags are saved identically.
3. **Asynchronous Reading & Writing**: `DatabaseManager` utilizes `CompletableFuture` to perform I/O operations off the main server thread, preventing TPS drops.
