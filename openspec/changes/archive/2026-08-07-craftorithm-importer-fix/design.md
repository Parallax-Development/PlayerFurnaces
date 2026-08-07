# Design Document: Craftorithm Importer Fix

## Overview

Fix namespace resolution and item property serialization for imported Craftorithm recipes.

## Technical Details

1. **Namespace Aliasing in `CraftorithmItemProvider`**:
   - `getNamespace()` returns `"craftorithm"`.
   - Update `isSimilar` and `getItem` calls or `ItemResolverRegistry` to handle both `"craftorithm"` and `"crafthorim"` namespace prefixes seamlessly.

2. **Item Extraction in `CraftorithmRecipeImporter`**:
   - For `RecipeChoice.MaterialChoice`: extract material list or sample material stack.
   - For `RecipeChoice.ExactChoice`: extract full stack metadata.
   - For both inputs and outputs: extract display name, lore list, custom model data, and non-craftorithm PDC tags.
   - Write output ID using `craftorithm:<item_id>`.

## Data Flow

```
[Craftorithm Cooking Recipe]
        │
        ▼
   Parse RecipeChoice (ExactChoice / MaterialChoice)
        │
        ▼
   Check PDC for Craftorithm ID -> "craftorithm:<item_id>"
   Else extract material, name, lore, CMD, PDC
        │
        ▼
   Save YAML in recipes/craftorithm/<recipe_id>.yml
```
