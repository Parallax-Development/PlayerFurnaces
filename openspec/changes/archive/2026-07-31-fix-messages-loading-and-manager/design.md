## Context

The `PlayerFurnaces` plugin includes a `messages.yml` in `src/main/resources`, but currently fails to save it to disk when enabling, and uses inline hardcoded strings inside command executors and GUI listeners.

## Goals / Non-Goals

**Goals:**
- Implement `MessageManager` for loading, caching, colorizing (`ChatColor.translateAlternateColorCodes`), and formatting message strings/lists.
- Ensure `messages.yml` is saved on startup using `saveResource("messages.yml", false)`.
- Support hot reloading of `messages.yml` via `/pfadmin reload`.
- Update commands (`PlayerFurnaceCommand`, `AdminCommand`) and GUI listeners to use `MessageManager`.

**Non-Goals:**
- Converting to MiniMessage / Kyori Adventure component format at this time (standard Spigot `ChatColor` with `&` translation keeps compatibility consistent with existing codebase).

## Decisions

- **Decision: `MessageManager` instance on `PlayerFurnacesPlugin`**
  - *Rationale*: Maintains symmetry with `RecipeManager`, `FuelManager`, `FurnaceManager`, and `DatabaseManager`.
  - *Alternative Considered*: Static utility class. Rejected to support clean lifecycle control and reloading per plugin instance.

- **Decision: Key-based message retrieval with placeholder replacement**
  - *Rationale*: Allows flexible placeholders like `{id}`, `{player}`, `{max}` passed via varargs or key-value maps.

## Risks / Trade-offs

- **[Risk]** Existing servers with customized `config.yml` might expect messages in `config.yml`.
  - *Mitigation*: Keep configuration distinct (`config.yml` for settings, `messages.yml` for user-facing texts).
