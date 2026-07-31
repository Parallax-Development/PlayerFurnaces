## 1. Resources & MessageManager Creation

- [x] 1.1 Update `src/main/resources/messages.yml` with comprehensive configurable keys and prefix options
- [x] 1.2 Create `MessageManager.java` in package `dev.darkblade.playerfurnaces.manager` to handle loading, reloading, color translation, and placeholder replacement

## 2. Plugin Integration & Commands Refactor

- [x] 2.1 Integrate `MessageManager` into `PlayerFurnacesPlugin.java` (save default resource, load on enable, reload on `/pfadmin reload`)
- [x] 2.2 Refactor `PlayerFurnaceCommand.java` to use `MessageManager` instead of hardcoded strings
- [x] 2.3 Refactor `AdminCommand.java` to use `MessageManager` instead of hardcoded strings
- [x] 2.4 Verify build with `./gradlew build`
