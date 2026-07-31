## 1. ItemProvider Infrastructure

- [x] 1.1 Create `ItemProvider` interface and `ItemResolverRegistry` for `namespace:item_id` lookups
- [x] 1.2 Implement `CraftorithmItemProvider` leveraging Craftorithm API / item registration
- [x] 1.3 Implement fallback `VanillaItemProvider` for `minecraft:` / local Bukkit materials

## 2. Recipe & Fuel Models and Managers

- [x] 2.1 Create `CustomRecipe` and `CustomFuel` data models
- [x] 2.2 Create `RecipeManager` to scan and parse `.yml` files in `plugins/PlayerFurnaces/recipes/`
- [x] 2.3 Create `FuelManager` to scan and parse `.yml` files in `plugins/PlayerFurnaces/fuels/`

## 3. Engine & Matching Integration

- [x] 3.1 Update `SmeltingManager` to match furnace input items against loaded `CustomRecipe` overrides before falling back to Bukkit recipes
- [x] 3.2 Update `FurnaceEngine` to support recipe-specific fuel restrictions, custom `cook-time-ticks`, `burn-time-ticks`, and experience rewards
- [x] 3.3 Add directory initialization and copy default `example.yml` on plugin startup
