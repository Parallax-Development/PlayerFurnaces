package dev.darkblade.playerfurnaces.model;

public class CustomFuel {

    private final String id;
    private final String type;
    private final int burnTimeTicks;

    public CustomFuel(String id, String type, int burnTimeTicks) {
        this.id = id;
        this.type = type;
        this.burnTimeTicks = burnTimeTicks;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getBurnTimeTicks() {
        return burnTimeTicks;
    }
}
