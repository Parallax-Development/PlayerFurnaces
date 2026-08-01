# ⚙️ Configuration Guide (`config.yml` & `messages.yml`)

This guide explains in detail each section of the primary configuration files in **PlayerFurnaces**.

---

## 📁 `config.yml`

The `config.yml` file controls the storage database engine, furnace limits per player, and GUI menu titles.

### 📄 Example `config.yml`

```yaml
database:
  # Available options: SQLITE, H2
  type: SQLITE
  file: furnaces.db

settings:
  # Default furnace count displayed in the main Hub GUI (1 to 54)
  default-furnace-count: 14
  # Absolute maximum furnace count limit in the system
  max-furnace-count: 54
  # GUI refresh rate in ticks (20 ticks = 1 second)
  gui-refresh-ticks: 10

gui:
  # Title of the main hub inventory
  hub-title: "&8{player}'s Furnaces"
  # Title of an individual furnace interface
  furnace-title: "&8Furnace #{id} - {status}"

recipes:
  vanilla-smelting:
    # Set to false to disable all vanilla Bukkit smelting recipes in custom furnaces.
    enabled: true
    # List of vanilla materials that cannot be smelted (only applies when enabled is true).
    disabled-materials:
      - RAW_IRON
      - ANCIENT_DEBRIS
```

### 🔍 Parameter Breakdown

| Parameter | Type | Description | Default Value |
| :--- | :--- | :--- | :--- |
| `database.type` | String | Database engine to use. Options: `SQLITE` (lightweight local file) or `H2` (MySQL-compatible mode). | `SQLITE` |
| `database.file` | String | Database file name inside `plugins/PlayerFurnaces/`. | `furnaces.db` |
| `settings.default-furnace-count` | Integer | Number of furnace slots displayed in player Hub menus (max 54). | `14` |
| `settings.max-furnace-count` | Integer | Absolute upper limit of furnaces allowed in the system. | `54` |
| `settings.gui-refresh-ticks` | Integer | Visual refresh frequency for open GUI inventories. | `10` |
| `gui.hub-title` | String | Hub inventory title. Supports the `{player}` placeholder. | `&8{player}'s Furnaces` |
| `gui.furnace-title` | String | Furnace inventory title. Supports `{id}` and `{status}`. | `&8Furnace #{id} - {status}` |
| `recipes.vanilla-smelting.enabled` | Boolean | Whether standard vanilla Bukkit smelting recipes can be processed as fallbacks in custom furnaces. | `true` |
| `recipes.vanilla-smelting.disabled-materials` | List<String> | List of vanilla material names (e.g., `RAW_IRON`) that are blocked from smelting when vanilla fallback is enabled. | `[]` |

---

## 💬 `messages.yml`

The `messages.yml` file contains all messages and notifications sent to players and administrators.

### 📄 Example `messages.yml`

```yaml
prefix: "&8[&ePlayerFurnaces&8] "
no-permission: "&cYou do not have permission to perform this action."
no-furnace-permission: "&cYou do not have permission to access Furnace #{id}."
furnace-not-found: "&cThe specified furnace does not exist or is unavailable."
player-not-found: "&cThe player was not found or their data has not loaded."
reload-success: "&aConfiguration and messages successfully reloaded!"
collect-success: "&aProcessed items collected successfully!"

admin-help:
  - "&e&lPlayerFurnaces Admin Commands:"
  - "&f/pfadmin view <player> [id] &7- View a player's furnace"
  - "&f/pfadmin reload &7- Reload configuration and recipes"
```

### 🎨 Supported Color Formatting
PlayerFurnaces supports:
* **Legacy Codes**: Using the `&` symbol (e.g., `&a`, `&e`, `&l`).
* **MiniMessage / Hexadecimal Syntax**: For modern gradients and RGB color tags (e.g., `<red>`, `<#FF5555>`).
