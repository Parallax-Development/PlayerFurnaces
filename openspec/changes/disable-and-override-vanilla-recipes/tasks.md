## 1. Configuration & Data Model Updates

- [x] 1.1 Update `config.yml` template to include `recipes.vanilla-smelting.enabled` and `recipes.vanilla-smelting.disabled-materials`.
- [x] 1.2 Update `CustomRecipe.java` model to include `boolean disabled`.
- [x] 1.3 Update `RecipeManager.java` to parse `disabled: true` from recipe files, and load/store global vanilla recipe controls from config.

## 2. Smelting Engine Logic Update

- [x] 2.1 Update `FurnaceEngine.java` to check if matched `CustomRecipe` is disabled, stopping smelting if so.
- [x] 2.2 Update `FurnaceEngine.java` to evaluate global `vanilla-smelting.enabled` and `disabled-materials` before running `SmeltingManager.getSmeltingRecipe(input)`.

## 3. Verification & Build

- [x] 3.1 Run `./gradlew build` to ensure project compiles cleanly without errors.
