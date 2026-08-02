## 1. Configuración Base

- [x] 1.1 Crear el archivo `menus.yml` por defecto en los recursos del plugin (`src/main/resources`).
- [x] 1.2 Mapear el contenido por defecto para el Hub y el View utilizando el sistema de Layout.
- [x] 1.3 Eliminar los keys antiguos relacionados con GUIs (`gui.hub.*` y `gui.furnace.*`) de `messages.yml` y limpiar el `MessageManager`.

## 2. Layout Engine (Motor de Plantillas)

- [x] 2.1 Crear clase `MenuConfig` para leer y cachear layouts, legendas (ítems, materiales, lore) y tipos de botones.
- [x] 2.2 Crear el parser del layout: una función que tome el layout y resuelva un mapa de `Map<Integer, MenuSlotData>`, asignando el slot correcto de 0 a (size-1).
- [x] 2.3 Implementar la lógica para slots dinámicos (`#`) en el Hub: asignar a cada `#` un índice secuencial (1, 2, 3...) y guardarlo en el mapa de resolución.

## 3. Actualización de las GUIs (Java)

- [x] 3.1 Refactorizar `FurnaceHubGui.java` para inyectar su inventario usando el `MenuConfig` cacheado, renderizando el relleno (fillers) según la leyenda.
- [x] 3.2 Actualizar el loop de hornos en `FurnaceHubGui` para que, en lugar de usar posiciones fijas `i-1`, busque en el mapa dinámico el slot asignado al horno `i`.
- [x] 3.3 Refactorizar `FurnaceViewGui.java` para construir su inventario basado en el layout de `menus.yml`.
- [x] 3.4 Asignar dinámicamente los items (`furnace.getInputItem()`, etc.) a los slots correspondientes resueltos (Input, Output, Fuel, Progreso, Indicador de Combustible, etc.).

## 4. Listeners y Acciones

- [x] 4.1 Modificar `GuiListener.java` para que evalúe los clicks basados en los slots dinámicos de `MenuConfig` en lugar de IDs de slot quemados en el código.
- [x] 4.2 Proteger los slots que sean `FILLER` o botones estáticos (como el de recolección, progreso, volver) para cancelar eventos `InventoryClickEvent` correctamente.

## 5. Pruebas y Ajustes Finales

- [x] 5.1 Probar la carga de `menus.yml` al reiniciar el servidor.
- [x] 5.2 Validar qué ocurre si un archivo `menus.yml` está mal formado o le faltan partes del layout.
- [x] 5.3 Comprobar que los hornos virtuales se sigan renderizando y funcionando con normalidad (smelting, recolección de items).
