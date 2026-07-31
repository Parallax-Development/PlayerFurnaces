## 1. Config & Manager Updates

- [x] 1.1 Update `src/main/resources/messages.yml` with English GUI translation keys (`gui.hub.*`, `gui.furnace.*`, `collection.*`)
- [x] 1.2 Add `getMessageList` helper to `MessageManager.java`

## 2. GUI Refactoring

- [x] 2.1 Refactor `FurnaceHubGui.java` to use `MessageManager` for all titles, item names, and lore lines
- [x] 2.2 Refactor `FurnaceViewGui.java` to use `MessageManager` for all titles, progress items, fuel indicators, collect button, and back button
- [x] 2.3 Refactor `GuiListener.java` to use `MessageManager` for collection feedback messages
- [x] 2.4 Verify build with `./gradlew build`
