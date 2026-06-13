// src/main/java/com/urceus/ucg/gui/generators/SummonGenerator.java
package com.urceus.ucg.gui.generators;

import com.urceus.ucg.gui.tabs.CommandGeneratorTab;
import com.urceus.ucg.util.CommandUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SummonGenerator extends CommandGenerator {
    private List<String> entityCache = new ArrayList<>();
    private boolean cacheLoaded = false;

    public SummonGenerator(CommandGeneratorTab tab) {
        super(tab);
        loadCacheAsync();
    }

    @Override
    public String getName() { return "SUMMON"; }

    @Override
    public String buildCommand(String target, String value, String amount) {
        if (value.isEmpty()) return "";
        
        StringBuilder cmd = new StringBuilder("/summon ");
        cmd.append(value);
        
        // Parse position from target field (format: x y z)
        String[] coords = target.split("\\s+");
        if (coords.length >= 3) {
            cmd.append(" ").append(coords[0]).append(" ").append(coords[1]).append(" ").append(coords[2]);
        } else {
            cmd.append(" ~ ~ ~");
        }
        
        // NBT from amount field
        if (!amount.isEmpty() && !amount.equals("1")) {
            cmd.append(" ").append(amount);
        }
        
        return cmd.toString();
    }

    @Override
    public void tick() {
        if (!cacheLoaded && cacheFuture != null && cacheFuture.isDone()) {
            cacheLoaded = true;
        }
    }

    @Override
    public List<String> getSuggestions(String input) {
        if (!cacheLoaded) return List.of();
        
        String lower = input.toLowerCase();
        return entityCache.stream()
            .filter(s -> s.toLowerCase().contains(lower))
            .limit(20)
            .collect(Collectors.toList());
    }

    private void loadCacheAsync() {
        cacheFuture = CompletableFuture.runAsync(() -> {
            List<String> temp = new ArrayList<>();
            for (EntityType<?> type : BuiltInRegistries.ENTITY_TYPE) {
                ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(type);
                temp.add(id.toString());
            }
            temp.sort(String::compareTo);
            this.entityCache = temp;
        });
    }
}