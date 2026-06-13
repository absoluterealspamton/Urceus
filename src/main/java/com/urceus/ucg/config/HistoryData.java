// src/main/java/com/urceus/ucg/config/HistoryData.java
package com.urceus.ucg.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urceus.ucg.UCGMod;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HistoryData {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE_PATH = Path.of(UCGMod.CONFIG_DIR, "history.json");
    private static final int MAX_HISTORY = 10;
    
    private final List<String> history = new ArrayList<>();

    public static CompletableFuture<HistoryData> loadAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (Files.exists(FILE_PATH)) {
                    String json = Files.readString(FILE_PATH);
                    return GSON.fromJson(json, HistoryData.class);
                }
            } catch (Exception e) {
                UCGMod.LOGGER.error("Failed to load history.json", e);
            }
            return new HistoryData();
        });
    }

    public void saveAsync() {
        CompletableFuture.runAsync(() -> {
            try {
                Files.createDirectories(FILE_PATH.getParent());
                String json = GSON.toJson(this);
                Files.writeString(FILE_PATH, json);
            } catch (Exception e) {
                UCGMod.LOGGER.error("Failed to save history.json", e);
            }
        });
    }

    public void addCommand(String command) {
        history.remove(command);
        history.add(0, command);
        if (history.size() > MAX_HISTORY) {
            history.remove(MAX_HISTORY);
        }
        saveAsync();
    }

    public List<String> getHistory() { return new ArrayList<>(history); }
}