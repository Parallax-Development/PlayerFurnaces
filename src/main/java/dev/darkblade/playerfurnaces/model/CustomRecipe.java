package dev.darkblade.playerfurnaces.model;

public class CustomRecipe {

    private final String id;
    private final RecipeItemDefinition input;
    private final RecipeItemDefinition result;
    private final int cookTimeTicks;
    private final double experience;
    private final String fuelType;
    private final Integer fuelBurnTicks;
    private final boolean disabled;

    public CustomRecipe(String id, RecipeItemDefinition input, RecipeItemDefinition result, int cookTimeTicks, double experience, String fuelType, Integer fuelBurnTicks) {
        this(id, input, result, cookTimeTicks, experience, fuelType, fuelBurnTicks, false);
    }

    public CustomRecipe(String id, RecipeItemDefinition input, RecipeItemDefinition result, int cookTimeTicks, double experience, String fuelType, Integer fuelBurnTicks, boolean disabled) {
        this.id = id;
        this.input = input;
        this.result = result;
        this.cookTimeTicks = cookTimeTicks <= 0 ? 200 : cookTimeTicks;
        this.experience = experience;
        this.fuelType = fuelType;
        this.fuelBurnTicks = fuelBurnTicks;
        this.disabled = disabled;
    }

    public String getId() {
        return id;
    }

    public RecipeItemDefinition getInput() {
        return input;
    }

    public RecipeItemDefinition getResult() {
        return result;
    }

    public int getCookTimeTicks() {
        return cookTimeTicks;
    }

    public double getExperience() {
        return experience;
    }

    public String getFuelType() {
        return fuelType;
    }

    public Integer getFuelBurnTicks() {
        return fuelBurnTicks;
    }

    public boolean isDisabled() {
        return disabled;
    }
}
