// src/main/java/com/urceus/ucg/config/SavedCommandsData.java
package com.urceus.ucg.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urceus.ucg.UCGMod;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class SavedCommandsData {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE_PATH = Path.of(UCGMod.CONFIG_DIR, "saved_commands.json");
    
    private final Map<String, SavedCommand> savedCommands = new LinkedHashMap<>();

    public static CompletableFuture<SavedCommandsData> loadAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (Files.exists(FILE_PATH)) {
                    String json = Files.readString(FILE_PATH);
                    return GSON.fromJson(json, SavedCommandsData.class);
                }
            } catch (Exception e) {
                UCGMod.LOGGER.error("Failed to load saved_commands.json", e);
            }
            return new SavedCommandsData();
        });
    }

    public void saveAsync() {
        CompletableFuture.runAsync(() -> {
            try {
                Files.createDirectories(FILE_PATH.getParent());
                String json = GSON.toJson(this);
                Files.writeString(FILE_PATH, json);
            } catch (Exception e) {
                UCGMod.LOGGER.error("Failed to save saved_commands.json", e);
            }
        });
    }

    public void saveCommand(String name, String command, String description) {
        savedCommands.put(name, new SavedCommand(name, command, description, System.currentTimeMillis()));
        saveAsync();
    }

    public void deleteCommand(String name) {
        savedCommands.remove(name);
        saveAsync();
    }

    public Collection<SavedCommand> getSavedCommands() { return savedCommands.values(); }
    public Optional<SavedCommand> getCommand(String name) { return Optional.ofNullable(savedCommands.get(name)); }

    public static class SavedCommand {
        public final String name;
        public final String command;
        public final String description;
        public final long timestamp;

        public SavedCommand(String name, String command, String description, long timestamp) {
            this.name = name;
            this.command = command;
            this.description = description;
            this.timestamp = timestamp;
        }
    }
}