# 🔑 Commands & Permissions

Reference manual listing all commands and permission nodes available in **PlayerFurnaces**.

---

## 🎮 Player Commands

### `/furnace`
* **Aliases**: `/furnaces`, `/horno`, `/hornos`, `/pf`
* **Permission**: `playerfurnaces.command.use`
* **Description**: Opens the graphical user interface (**Virtual Furnace Hub**) containing all of the player's furnaces.
* **Usage with arguments**: `/furnace <id>` (Example: `/furnace 1`) - Directly opens virtual furnace number `<id>` if the player has the required access permission.

---

## 👮 Admin Commands

### `/playerfurnacesadmin`
* **Aliases**: `/pfadmin`, `/furnacesadmin`
* **Permission**: `playerfurnaces.admin`
* **Description**: Root command for administrative tasks.

#### Subcommands:
1. **`/pfadmin view <player> [id]`**
   * **Description**: Allows an administrator to inspect the furnace hub or a specific virtual furnace of any player, online or offline.
   * **Example**: `/pfadmin view DarkBladeDev 2`
2. **`/pfadmin force-open <player> <index> [--bypass-perms]`**
   * **Description**: Forces an online target player to open their specified virtual furnace index. Respects standard target furnace permissions (`playerfurnaces.furnace.<index>`) unless `--bypass-perms` (or `-b`) is passed.
   * **Example**: `/pfadmin force-open Steve 1` or `/pfadmin force-open Alex 3 --bypass-perms`
3. **`/pfadmin reload`**
   * **Description**: Hot reloads `config.yml`, `messages.yml`, `menus.yml`, as well as all recipe (`recipes/`) and fuel (`fuels/`) files.

---

## 🔒 Permission Node Structure

PlayerFurnaces uses a granular permission structure to control access to virtual furnaces:

| Permission Node | Default Value | Description |
| :--- | :--- | :--- |
| `playerfurnaces.command.use` | `true` (Everyone) | Grants permission to execute the base `/furnace` command. |
| `playerfurnaces.admin` | `op` (Operators) | Grants full access to `/pfadmin` commands. |
| `playerfurnaces.furnace.1` | `op` | Grants access to Virtual Furnace #1. |
| `playerfurnaces.furnace.2` | `op` | Grants access to Virtual Furnace #2. |
| `playerfurnaces.furnace.<3-54>` | `false` | Unlocks individual furnace `<id>`. Ideal for VIP ranks or rewards. |

---

## 💡 LuckPerms Setup Examples

### Granting access to 5 virtual furnaces for a VIP rank
```bash
/lp group vip permission set playerfurnaces.furnace.1 true
/lp group vip permission set playerfurnaces.furnace.2 true
/lp group vip permission set playerfurnaces.furnace.3 true
/lp group vip permission set playerfurnaces.furnace.4 true
/lp group vip permission set playerfurnaces.furnace.5 true
```

### Denying admin commands for moderators
```bash
/lp group mod permission set playerfurnaces.admin false
```
