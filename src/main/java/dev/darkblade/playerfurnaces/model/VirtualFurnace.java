package dev.darkblade.playerfurnaces.model;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class VirtualFurnace {
    private final UUID ownerUuid;
    private final int furnaceId;
    private String customName;

    private ItemStack inputItem;
    private ItemStack fuelItem;
    private ItemStack outputItem;

    private int cookTime;
    private int totalCookTime;
    private int burnTime;
    private int totalBurnTime;
    private long lastUpdatedTimestamp;

    public VirtualFurnace(UUID ownerUuid, int furnaceId) {
        this.ownerUuid = ownerUuid;
        this.furnaceId = furnaceId;
        this.customName = null;
        this.cookTime = 0;
        this.totalCookTime = 200;
        this.burnTime = 0;
        this.totalBurnTime = 0;
        this.lastUpdatedTimestamp = System.currentTimeMillis();
    }

    public UUID getOwnerUuid() {
        return ownerUuid;
    }

    public int getFurnaceId() {
        return furnaceId;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public ItemStack getInputItem() {
        return inputItem;
    }

    public void setInputItem(ItemStack inputItem) {
        this.inputItem = inputItem;
    }

    public ItemStack getFuelItem() {
        return fuelItem;
    }

    public void setFuelItem(ItemStack fuelItem) {
        this.fuelItem = fuelItem;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public void setOutputItem(ItemStack outputItem) {
        this.outputItem = outputItem;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public int getTotalCookTime() {
        return totalCookTime;
    }

    public void setTotalCookTime(int totalCookTime) {
        this.totalCookTime = totalCookTime;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public void setBurnTime(int burnTime) {
        this.burnTime = burnTime;
    }

    public int getTotalBurnTime() {
        return totalBurnTime;
    }

    public void setTotalBurnTime(int totalBurnTime) {
        this.totalBurnTime = totalBurnTime;
    }

    public long getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp(long lastUpdatedTimestamp) {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }

    public FurnaceStatus getStatus() {
        if (burnTime > 0 && inputItem != null && inputItem.getAmount() > 0) {
            return FurnaceStatus.SMELTING;
        }
        if (inputItem != null && inputItem.getAmount() > 0 && (burnTime <= 0 && (fuelItem == null || fuelItem.getAmount() == 0))) {
            return FurnaceStatus.NO_FUEL;
        }
        return FurnaceStatus.IDLE;
    }
}
