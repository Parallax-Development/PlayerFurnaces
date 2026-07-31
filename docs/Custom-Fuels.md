# 🔥 Custom Fuels Guide (`fuels/*.yml`)

**PlayerFurnaces** allows defining global custom fuels in the `plugins/PlayerFurnaces/fuels/` directory. These custom fuels can be used across any virtual furnace and supplement or replace default vanilla burn durations.

---

## 📁 Fuel File Structure

Each `.yml` file inside the `fuels/` directory contains one or multiple fuel definitions identified by a root YAML key.

### 📄 Example (`fuels/hyper_coal.yml`)

```yaml
hyper_coal: # Unique global fuel identifier
  type: "COAL" # Base Bukkit material or namespaced provider ID (e.g., 'crafthorim:hyper_coal' or 'COAL')
  burn-time-ticks: 1000 # Total burn duration in ticks (20 ticks = 1 second; 1000 ticks = 50 seconds)
```

---

## ⚙️ Configuration Parameters

| Parameter | Type | Required | Description |
| :--- | :--- | :--- | :--- |
| `type` | String | Yes | Fuel item identifier. Can be a vanilla material (e.g., `COAL`, `BLAZE_ROD`), a namespaced third-party item ID (e.g., `crafthorim:hyper_coal`), or matched via item resolvers. |
| `burn-time-ticks` | Integer | Yes | Total duration one unit of this fuel burns (in ticks). |

---

## 💡 Burn Duration Reference

* **20 Ticks** = 1 Second.
* **200 Ticks** = 10 Seconds (vanilla cooking duration for 1 basic item).
* **1600 Ticks** = 80 Seconds (vanilla burn time for 1 Coal).
* **20000 Ticks** = 1000 Seconds (vanilla burn time for 1 Lava Bucket).

### 🔄 Fuel Registration Priority
When an item stack is placed in the fuel slot (Slot 29), the plugin determines its burn time following this priority order:

```
  1. Active recipe fuel override (`fuel.burn-time-ticks` in active recipe)
         │
         ▼
  2. Custom global fuel definition (`fuels/*.yml`)
         │
         ▼
  3. Bukkit/Minecraft Vanilla burn time
```
