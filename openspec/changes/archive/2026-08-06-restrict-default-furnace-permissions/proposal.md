# Proposal: Restrict Default Furnace Permissions

## Summary
Update the default permission settings for `playerfurnaces.furnace.1` and `playerfurnaces.furnace.2` in `plugin.yml` from `true` to `op`.

## Motivation
Currently, all players automatically receive permissions for furnaces 1 and 2 by default because `plugin.yml` sets `default: true`. Setting these to `default: op` ensures normal players only receive access when explicitly granted permissions via a permission plugin like LuckPerms.

## Scope
- Modify `plugin.yml` permission defaults for `playerfurnaces.furnace.1` and `playerfurnaces.furnace.2`.
- Update README documentation if permission defaults are mentioned.
