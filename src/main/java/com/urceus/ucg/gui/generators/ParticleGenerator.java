// src/main/java/com/urceus/ucg/gui/generators/ParticleGenerator.java
package com.urceus.ucg.gui.tabs.CommandGeneratorTab;

import com.urceus.ucg.gui.tabs.CommandGeneratorTab;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ParticleGenerator extends CommandGenerator {
    private List<String> particleCache = new ArrayList<>();
    private boolean cacheLoaded = false;

    public ParticleGenerator(CommandGeneratorTab tab) { super(tab); loadCacheAsync(); }

    @Override public String getName() { return "PARTICLE"; }

    @Override
    public String buildCommand(String target, String value, String amount) {
        if (value.isEmpty()) return "";
        
        StringBuilder cmd = new StringBuilder("/particle ");
        cmd.append(value).append(" ");
        
        // Parse coordinates from target
        String[] coords = target.split("\\s+");
        if (coords.length >= 3) {
            cmd.append(coords[0]).append(" ").append(coords[1]).append(" ").append(coords[2]);
        } else {
            cmd.append("~ ~ ~");
        }
        
        cmd.append(" 0 0 0 0 ").append(amount.isEmpty() ? "10" : amount).append(" normal");
        return cmd.toString();
    }

    @Override public void tick() {
        if (!cacheLoaded && cacheFuture != null && cacheFuture.isDone()) cacheLoaded = true;
    }

    @Override public List<String> getSuggestions(String input) {
        if (!cacheLoaded) return List.of();
        String lower = input.toLowerCase();
        return particleCache.stream().filter(s -> s.toLowerCase().contains(lower)).limit(20).collect(Collectors.toList());
    }

    private void loadCacheAsync() {
        cacheFuture = CompletableFuture.runAsync(() -> {
            List<String> temp = new ArrayList<>();
            for (ParticleType<?> type : BuiltInRegistries.PARTICLE_TYPE) {
                temp.add(BuiltInRegistries.PARTICLE_TYPE.getKey(type).toString());
            }
            temp.sort(String::compareTo);
            this.particleCache = temp;
        });
    }
}