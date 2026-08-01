## 1. Recipe Validation Implementation

- [x] 1.1 Add item definition validation helper to `RecipeManager` to verify material names and item provider namespaces for input/result sections.
- [x] 1.2 Add detailed `WARNING` logging in `RecipeManager` for invalid material or provider references during recipe parsing, specifying file name, recipe ID, section, and invalid value.
- [x] 1.3 Track skipped recipes due to configuration errors in `RecipeManager` and report total loaded vs. skipped counts in the plugin log.

## 2. Fuel Validation Implementation

- [x] 2.1 Add fuel type validation logic to `FuelManager` to verify materials and third-party item provider namespace IDs.
- [x] 2.2 Add `WARNING` logging in `FuelManager` when encountering an invalid material or provider identifier in fuel configurations.
- [x] 2.3 Track skipped fuels due to configuration errors in `FuelManager` and report total loaded vs. skipped counts in the plugin log.

## 3. Verification & Testing

- [x] 3.1 Verify project builds cleanly with `mvn clean package`.
- [x] 3.2 Add test cases or manual verification scenarios for invalid material inputs in recipes and fuels.
