## Context

Actualmente, las interfaces de usuario del plugin (`FurnaceHubGui` y `FurnaceViewGui`) están programadas directamente (hardcoded) en las clases Java. Esto abarca tamaños de inventario, posiciones de botones, materiales de decoración (fillers) e íconos de estado. Para un servidor de Minecraft, esta rigidez impide la integración estética del plugin con otros sistemas personalizados.

## Goals / Non-Goals

**Goals:**
- Implementar un motor de plantillas (layout engine) que traduzca arreglos de strings en `menus.yml` a Inventarios de Bukkit.
- Centralizar la configuración visual (materiales, nombres, lore, slots) en un solo archivo.
- Soportar slots dinámicos para los hornos del jugador (usando el símbolo `#` o similar).
- Mapear las acciones de los botones (clicks) a los slots resueltos dinámicamente en lugar de slots fijos en Java.

**Non-Goals:**
- No se implementarán animaciones frame-por-frame complejas por ahora.
- No se dará soporte para inventarios de tipos especiales que no sean `CHEST` genéricos, a menos que el tamaño determine automáticamente filas (múltiplos de 9).

## Decisions

- **Uso de un sistema de Layouts por caracteres:** 
  - *Rationale:* Es el estándar moderno en plugins premium y es más intuitivo que definir slot por slot (ej. `slot: 11`, `slot: 12`).
  - *Alternativa considerada:* Definición por slots fijos. Descartada porque dificulta visualizar el resultado final.
- **Migración de Textos:**
  - *Rationale:* Los nombres y descripciones de los botones se moverán de `messages.yml` a `menus.yml`. Esto unifica la configuración visual.
- **Slots Dinámicos (`#`):**
  - *Rationale:* En el `FurnaceHubGui`, el número de hornos puede variar. El layout definirá las posiciones permitidas (ej. 14 símbolos `#`). El código escaneará el layout y guardará un mapeo de "Índice de horno -> Slot del inventario".
- **Carga en memoria:**
  - *Rationale:* El archivo `menus.yml` se cargará al iniciar el plugin y las estructuras de los menús se parsearán una sola vez y se mantendrán en memoria para evitar lag al abrir inventarios.

## Risks / Trade-offs

- **Risk:** Errores de sintaxis en `menus.yml` (ej. layouts que no son múltiplos de 9, o caracteres usados en layout pero no definidos en la leyenda).
  - *Mitigation:* Validación estricta durante el startup. Si falla, se escribirá un error claro en consola y se cargará un menú default (hardcoded como fallback o un menú de error visual).
- **Risk:** Interacciones con otros plugins de gestión de inventarios.
  - *Mitigation:* Asegurarse de que `InventoryHolder` se siga usando correctamente para identificar nuestros menús y cancelar los eventos `InventoryClickEvent` de forma segura.
