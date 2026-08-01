# 🧩 External Plugin Integration & ItemProvider API

PlayerFurnaces features an extensible architecture called **`ItemResolverRegistry`** that allows recognizing and seamlessly interacting with items created by third-party plugins (such as **Craftorithm**, **Oraxen**, **ItemsAdder**, **MMOItems**, etc.).

---

## 🔍 Namespaced Item Resolution (`namespace:item_id`)

When a custom recipe or fuel specifies an item using the format:

```text
namespace:item_id
```

*(For example: `crafthorim:ruby_ingot` or `oraxen:mythic_coal`)*

PlayerFurnaces queries its provider registry for a registered `ItemProvider` that handles the given `namespace`.

```
                        ┌─────────────────────────────────┐
                        │   ItemResolverRegistry          │
                        └────────────────┬────────────────┘
                                         │
                 ┌───────────────────────┴───────────────────────┐
                 ▼                                               ▼
      ┌─────────────────────┐                         ┌─────────────────────┐
      │ VanillaItemProvider │                         │CraftorithmProvider  │
      │ (Namespace: vanilla)│                         │(Namespace:crafthorim│
      └─────────────────────┘                         └─────────────────────┘
```

---

## 🛠️ Native Craftorithm Integration

PlayerFurnaces automatically connects with **Craftorithm** via reflection and native APIs:

1. If the **Craftorithm** plugin is enabled on the server, `CraftorithmItemProvider` is automatically registered upon startup.
2. Recipes using `crafthorim:item_id` receive original `ItemStack` objects from Craftorithm with all custom textures, attributes, and PDC tags intact.
3. If an item stack cannot be directly constructed via API methods, the system performs a cross-check using tags in the item's `PersistentDataContainer` (`craftorithm:item_id`).

---

## 💻 Developer Guide: Creating a Custom `ItemProvider`

To integrate your custom item plugin with **PlayerFurnaces**, simply implement the `ItemProvider` interface and register it into `ItemResolverRegistry`.

### 1. Implementing the Interface

```java
package dev.darkblade.playerfurnaces.provider;

import org.bukkit.inventory.ItemStack;

public interface ItemProvider {

    /**
     * Returns the namespace handled by this provider.
     * Example: "myplugin" for items specified as "myplugin:item_id".
     */
    String getNamespace();

    /**
     * Constructs an ItemStack from its unique ID and amount.
     */
    ItemStack getItem(String id, int amount);

    /**
     * Compares an in-game ItemStack with the specified ID.
     */
    boolean isSimilar(ItemStack itemStack, String id);
}
```

### 2. Registering in the Plugin

```java
ItemResolverRegistry registry = PlayerFurnacesPlugin.getInstance().getItemResolverRegistry();
registry.registerProvider(new MyPluginItemProvider());
```
