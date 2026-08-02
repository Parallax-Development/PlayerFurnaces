package dev.darkblade.playerfurnaces.model;

import java.util.HashMap;
import java.util.Map;

public class MenuLayout {
    private final String title;
    private final int size;
    private final Map<Integer, MenuSlotData> slots;
    private final Map<Integer, Integer> dynamicSlotMap; // Furnace Index (1, 2, 3...) -> Inventory Slot (0..size)

    public MenuLayout(String title, int size) {
        this.title = title;
        this.size = size;
        this.slots = new HashMap<>();
        this.dynamicSlotMap = new HashMap<>();
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public Map<Integer, MenuSlotData> getSlots() {
        return slots;
    }

    public Map<Integer, Integer> getDynamicSlotMap() {
        return dynamicSlotMap;
    }
}
