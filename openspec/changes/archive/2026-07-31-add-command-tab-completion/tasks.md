## 1. Player Command Tab Completion

- [x] 1.1 Update `PlayerFurnaceCommand` to implement `TabExecutor` and implement `onTabComplete` with permission checks and furnace ID filtering.

## 2. Admin Command Tab Completion

- [x] 2.1 Update `AdminCommand` to implement `TabExecutor` and implement `onTabComplete` for subcommands (`reload`, `view`), online player names, and furnace IDs.

## 3. Verification & Testing

- [x] 3.1 Create unit test to verify `onTabComplete` outputs for both commands under different permissions and arguments.
- [x] 3.2 Build project via `./gradlew build` to confirm compilation without errors.
