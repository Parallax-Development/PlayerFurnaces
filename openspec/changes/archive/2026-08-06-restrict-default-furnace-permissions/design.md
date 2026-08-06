# Design: Restrict Default Furnace Permissions

## Architecture Changes
No Java code changes are needed. The permission checking logic in `FurnaceManager.hasPermissionForFurnace(...)` remains unchanged.

## Configuration Changes
In `src/main/resources/plugin.yml`:
- Set `playerfurnaces.furnace.1.default` to `op`
- Set `playerfurnaces.furnace.2.default` to `op`
