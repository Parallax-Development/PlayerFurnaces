## Why

Actualmente los diseños de los menús visuales (`FurnaceHubGui` y `FurnaceViewGui`) están forzados (hardcoded) en el código fuente de Java, desde el tamaño del inventario hasta los slots fijos de cada ícono y los materiales de relleno. 
Esto limita enormemente la capacidad de los administradores para personalizar su servidor. Permitir diseños de paneles 100% configurables usando un sistema visual de "layouts" diferenciará a PlayerFurnaces brindando un nivel premium de personalización a los dueños de los servidores.

## What Changes

- **BREAKING**: Los menús del plugin ya no tendrán tamaños ni layouts fijos en código.
- **BREAKING**: Los nombres y descripciones de los botones de los menús (anteriormente en `messages.yml`) pasarán a ser definidos dentro de la configuración del menú para centralizar el diseño.
- Se implementará un nuevo archivo `menus.yml` que soporte configuración mediante un sistema de caracteres de "layout" visual.
- El Hub (menú principal) usará un carácter especial (ej. `#`) que el plugin buscará dinámicamente y reemplazará en orden por los hornos virtuales desbloqueados o bloqueados.
- El View (menú del horno) usará caracteres para especificar el botón de Input, Output, Fuel, Progreso, Recolectar, Volver, etc.
- Todas las decoraciones/rellenos y materiales de estado de los ítems podrán ser completamente configurados en `menus.yml`.

## Capabilities

### New Capabilities
- `custom-gui-layouts`: Define el sistema de plantillas (layouts) basadas en caracteres para dibujar dinámicamente inventarios en Bukkit con sus respectivos estados de botones, materiales y decoraciones.

### Modified Capabilities
- `gui-localization`: Los textos de los menús migran su fuente de verdad hacia el sistema de `custom-gui-layouts`.
- `virtual-furnaces`: Las interacciones del jugador (click en inventarios) deberán conectarse dinámicamente al slot resuelto por el layout en lugar de slots fijos.

## Impact

- **Código Afectado**: Paquete `dev.darkblade.playerfurnaces.gui`, específicamente `FurnaceHubGui`, `FurnaceViewGui`, `GuiListener`.
- **Configuraciones**: Se añadirá `menus.yml` y requerirá eliminar mensajes antiguos de `messages.yml`.
- **Interacciones**: `GuiListener` deberá mapear las posiciones de los clicks basadas en los slots guardados en memoria al procesar el layout de `menus.yml`, en lugar de hacer `if (slot == 11)` etc.
