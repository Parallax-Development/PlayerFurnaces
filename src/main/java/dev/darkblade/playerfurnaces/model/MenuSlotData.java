package dev.darkblade.playerfurnaces.model;

import java.util.HashMap;
import java.util.Map;

public class MenuSlotData {
    private final String type;
    private final MenuStateData defaultState;
    private final Map<String, MenuStateData> states;

    public MenuSlotData(String type, MenuStateData defaultState, Map<String, MenuStateData> states) {
        this.type = type;
        this.defaultState = defaultState;
        this.states = states != null ? states : new HashMap<>();
    }

    public String getType() {
        return type;
    }

    public MenuStateData getDefaultState() {
        return defaultState;
    }

    public MenuStateData getState(String stateName) {
        return states.getOrDefault(stateName, defaultState);
    }
}
