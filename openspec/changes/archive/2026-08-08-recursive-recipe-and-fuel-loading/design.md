## Context

Currently, `RecipeManager` and `FuelManager` scan their respective data folders (`plugins/PlayerFurnaces/recipes/` and `plugins/PlayerFurnaces/fuels/`) using `File.listFiles(FilenameFilter)`. This method only lists files in the immediate root of those folders. If administrators organize recipe or fuel files into subdirectories (e.g. `recipes/ores/rubies.yml` or `fuels/magic/coal.yml`), those files are ignored.

## Goals / Non-Goals

**Goals:**
- Recursively scan `recipes/` and all subdirectories for `.yml` and `.yaml` files in `RecipeManager`.
- Recursively scan `fuels/` and all subdirectories for `.yml` and `.yaml` files in `FuelManager`.
- Ensure robust exception and resource handling when walking the directory tree.
- Maintain existing warning logs for malformed files and summary logs for registered vs skipped counts.

**Non-Goals:**
- Implementing per-folder category structures or folder-based permissions.

## Decisions

### Decision 1: Use `Files.walk()` for recursive file discovery

Rather than writing manual recursive directory traversal helpers with `File.listFiles()`, we will use Java's `Files.walk(Path)` wrapped in a try-with-resources block.

**Rationale:**
- `Files.walk()` is clean, idiomatic Java NIO, and automatically handles nested subdirectories of arbitrary depth.
- Filtering with `Files.isRegularFile(path)` and checking filename extensions (`.yml`, `.yaml`) prevents attempting to parse subdirectories as YAML configuration files.

**Alternatives Considered:**
- Manual recursive helper using `File.listFiles()`: Requires handling null file arrays and manual recursion logic. `Files.walk()` is cleaner and less error-prone.

## Risks / Trade-offs

- **[Risk]** Deep directory trees or circular symlinks could impact startup time.
  - **Mitigation:** Standard configuration directories in Minecraft plugin data folders are small. `Files.walk()` without `FOLLOW_LINKS` prevents infinite loops on circular symlinks.
