// src/main/java/com/urceus/ucg/config/ClicksData.java
package com.urceus.ucg.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urceus.ucg.UCGMod;
import net.minecraft.util.GsonHelper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public class ClicksData {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE_PATH = Path.of(UCGMod.CONFIG_DIR, "clicks.json");
    
    private int clickCount = 0;
    private boolean isLocked = false;
    private long lockStartDayTime = 0;
    private long lastClickTime = 0;

    public static CompletableFuture<ClicksData> loadAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (Files.exists(FILE_PATH)) {
                    String json = Files.readString(FILE_PATH);
                    return GSON.fromJson(json, ClicksData.class);
                }
            } catch (Exception e) {
                UCGMod.LOGGER.error("Failed to load clicks.json", e);
            }
            return new ClicksData();
        });
    }

    public void saveAsync() {
        CompletableFuture.runAsync(() -> {
            try {
                Files.createDirectories(FILE_PATH.getParent());
                String json = GSON.toJson(this);
                Files.writeString(FILE_PATH, json);
            } catch (Exception e) {
                UCGMod.LOGGER.error("Failed to save clicks.json", e);
            }
        });
    }

    public void incrementClick() {
        this.clickCount++;
        this.lastClickTime = System.currentTimeMillis();
        saveAsync();
    }

    public void triggerLock(long currentDayTime) {
        this.isLocked = true;
        this.lockStartDayTime = currentDayTime;
        saveAsync();
    }

    public boolean checkLockExpired(long currentDayTime) {
        if (!isLocked) return true;
        if (currentDayTime - lockStartDayTime >= 24000L) {
            this.isLocked = false;
            this.lockStartDayTime = 0;
            saveAsync();
            return true;
        }
        return false;
    }

    // Getters & Setters
    public int getClickCount() { return clickCount; }
    public void setClickCount(int clickCount) { this.clickCount = clickCount; }
    public boolean isLocked() { return isLocked; }
    public void setLocked(boolean locked) { isLocked = locked; }
    public long getLockStartDayTime() { return lockStartDayTime; }
    public long getLastClickTime() { return lastClickTime; }
}