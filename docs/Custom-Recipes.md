# 🧪 Custom Recipes Guide (`recipes/*.yml`)

**PlayerFurnaces** features a modular recipe engine allowing server owners to override vanilla furnace recipes or create completely custom recipes using `.yml` files placed inside `plugins/PlayerFurnaces/recipes/` (including subdirectories).

---

## 📁 Recipe File Structure

Each `.yml` file inside the `recipes/` directory can contain one or multiple recipe definitions. Each root YAML key represents a **Unique Recipe ID**.

### 📄 Complete Example (`recipes/example.yml`)

```yaml
crafthorim_ruby_smelt: # Unique recipe ID.
  input:
    id: ruby_ore # Descriptive ID for local or third-party items
    material: REDSTONE_ORE # Base Bukkit material
    name: "<red>Ruby Ore" # Custom name with MiniMessage or '&'
    lore:
      - ""
      - "<gray><b>Mining Material"
    custom-model-data: 1005 # Optional: Minecraft CustomModelData
    pdc:
      "myplugin:item_id": "ruby_ore" # Optional: PersistentDataContainer tags

  result:
    # Imported directly from Craftorithm
    id: "crafthorim:ruby_ingot" # Namespaced ID format
    amount: 1

  cook-time-ticks: 100 # Cooking duration in ticks (20 ticks = 1 second; 100 ticks = 5 seconds)
  experience: 2.5 # Experience awarded upon smelting

fuel:
  type: "hyper_coal" # References custom fuel defined in fuels/hyper_coal.yml
  burn-time-ticks: 1000 # Optional burn time override for this recipe (in ticks)
```

---

## ⚙️ Recipe Syntax & Attributes

### 📥 1. Input (`input`)
Defines the required item properties in the input slot (Slot 11) to trigger this recipe:

| Field | Type | Required | Description |
| :--- | :--- | :--- | :--- |
| `id` | String | No | Item or provider identifier. If using `namespace:item_id` format (e.g., `crafthorim:ruby_ore`), verification is delegated to the respective provider. |
| `material` | Material | Yes (unless namespaced) | Bukkit vanilla material name (e.g., `REDSTONE_ORE`, `RAW_IRON`, `GOLD_ORE`). |
| `name` | String | No | Formatted item name required on the input item stack. |
| `lore` | List<String> | No | Required lore lines on the input item stack. |
| `custom-model-data` | Integer | No | Required `CustomModelData` integer value. |
| `pdc` | Map<String, String> | No | Key-value pairs required in the item's `PersistentDataContainer`. |

---

### 📤 2. Result (`result`)
Defines the output item stack generated in the output slot (Slot 15):

| Field | Type | Required | Description |
| :--- | :--- | :--- | :--- |
| `id` | String | No | If specified in `namespace:item_id` format (e.g., `crafthorim:ruby_ingot`), the item stack is automatically resolved via the corresponding provider. |
| `material` | Material | Yes (if no provider `id`) | Vanilla Bukkit material of the result item. |
| `amount` | Integer | No (default: 1) | Quantity of items produced per completed smelting cycle. |

---

### ⏱️ 3. Timings & Experience

| Field | Type | Default | Description |
| :--- | :--- | :--- | :--- |
| `cook-time-ticks` | Integer | `200` (10s) | Required duration to smelt one item (in ticks). |
| `experience` | Double | `0.0` | Amount of floating experience awarded upon completion. |

---

### 🔥 4. Fuel Constraint (`fuel`)

You can restrict a recipe to only smelt when a specific fuel type is provided:

```yaml
fuel:
  type: "hyper_coal"      # Fuel identifier (custom fuel, vanilla material, or namespaced item)
  burn-time-ticks: 1200   # Optional burn duration override specific to this recipe
```

* **`type`**: Can be a custom fuel identifier (`hyper_coal`), a vanilla material (`COAL`, `LAVA_BUCKET`), or a namespaced provider item (`crafthorim:solar_fuel`).
* **`burn-time-ticks`**: (Optional) If defined, overrides the standard fuel burn time whenever this specific recipe is smelting.

---

## 🚫 Vanilla Recipe Overrides & Disabling

PlayerFurnaces allows server administrators to override or disable vanilla Minecraft smelting recipes:

### 1. Overriding Vanilla Recipes
Any custom recipe defined in `recipes/*.yml` that matches a vanilla input material (e.g., `RAW_IRON`) takes precedence over standard Minecraft recipes. You can customize the result, cooking time, experience, or fuel requirement:

```yaml
custom_iron_smelting:
  input:
    material: RAW_IRON
  result:
    material: IRON_INGOT
    amount: 2                  # Yields 2 ingots instead of 1
  cook-time-ticks: 100         # Smelts in 5 seconds instead of 10
```

### 2. Disabling Specific Vanilla Items via Recipe YML (`disabled: true`)
You can modularly disable smelting for a specific item by setting `disabled: true` on a recipe entry:

```yaml
disable_porkchop:
  input:
    material: PORKCHOP
  disabled: true              # Disables smelting for raw porkchops in custom furnaces
```

### 3. Global & Material Blacklist Controls (`config.yml`)
Vanilla smelting fallbacks can also be managed globally in `config.yml`:

```yaml
recipes:
  vanilla-smelting:
    enabled: false             # Set to false to disable ALL vanilla smelting recipes
    disabled-materials:       # Or disable specific vanilla materials
      - RAW_IRON
      - ANCIENT_DEBRIS
```

