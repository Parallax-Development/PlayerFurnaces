# Tasks: Recipe Import Command

- [x] Create `RecipeImporter` interface and `ImportResult` model <!-- id: 0 -->
- [x] Create `RecipeImporterRegistry` to manage supported plugin importers <!-- id: 1 -->
- [x] Implement `CraftorithmRecipeImporter` using safe Java reflection for Craftorithm API <!-- id: 2 -->
- [x] Register `RecipeImporterRegistry` in `PlayerFurnacesPlugin` <!-- id: 3 -->
- [x] Update `AdminCommand` to handle `/pfa import <plugin> [--overwrite|-f]` and tab-completion <!-- id: 4 -->
- [x] Add message keys in `messages.yml` for import success, errors, usage, and skipped count <!-- id: 5 -->
- [x] Verify build via `./gradlew build` <!-- id: 6 -->

