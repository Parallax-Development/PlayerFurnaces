## 1. RecipeManager Recursive Loading

- [x] 1.1 Update `RecipeManager.loadRecipes()` to use `Files.walk()` for recursively scanning `.yml` and `.yaml` files in `recipes/` and its subdirectories.
- [x] 1.2 Add unit test in `RecipeManagerTest` verifying recursive loading of recipes in nested subdirectories.

## 2. FuelManager Recursive Loading

- [x] 2.1 Update `FuelManager.loadFuels()` to use `Files.walk()` for recursively scanning `.yml` and `.yaml` files in `fuels/` and its subdirectories.
- [x] 2.2 Add unit test in `FuelManagerTest` verifying recursive loading of fuels in nested subdirectories.

## 3. Verification & Build

- [x] 3.1 Execute `./gradlew test` and verify that all unit tests pass clean.
- [x] 3.2 Execute `./gradlew build` to confirm clean plugin compilation.
