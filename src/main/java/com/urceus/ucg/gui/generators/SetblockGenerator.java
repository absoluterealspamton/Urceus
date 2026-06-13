// src/main/java/com/urceus/ucg/gui/generators/SetblockGenerator.java
package com.urceus.ucg.gui.generators;

import com.urceus.ucg.gui.tabs.CommandGeneratorTab;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SetblockGenerator extends CommandGenerator {
    private List<String> blockCache = new ArrayList<>();
    private boolean cacheLoaded = false;

    public SetblockGenerator(CommandGeneratorTab tab) { super(tab); loadCacheAsync(); }

    @Override public String getName() { return "SETBLOCK"; }

    @Override
    public String buildCommand(String target, String value, String amount) {
        if (value.isEmpty()) return "";
        String[] coords = target.split("\\s+");
        String pos = coords.length >= 3 ? coords[0] + " " + coords[1] + " " + coords[2] : "~ ~ ~";
        return "/setblock " + pos + " " + value + (amount.isEmpty() ? "" : " " + amount);
    }

    @Override public void tick() {
        if (!cacheLoaded && cacheFuture != null && cacheFuture.isDone()) cacheLoaded = true;
    }

    @Override public List<String> getSuggestions(String input) {
        if (!cacheLoaded) return List.of();
        String lower = input.toLowerCase();
        return blockCache.stream().filter(s -> s.toLowerCase().contains(lower)).limit(20).collect(Collectors.toList());
    }

    private void loadCacheAsync() {
        cacheFuture = CompletableFuture.runAsync(() -> {
            List<String> temp = new ArrayList<>();
            for (Block block : BuiltInRegistries.BLOCK) {
                temp.add(BuiltInRegistries.BLOCK.getKey(block).toString());
            }
            temp.sort(String::compareTo);
            this.blockCache = temp;
        });
    }
}