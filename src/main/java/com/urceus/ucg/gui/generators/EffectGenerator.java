// src/main/java/com/urceus/ucg/gui/generators/EffectGenerator.java
package com.urceus.ucg.gui.generators;

import com.urceus.ucg.gui.tabs.CommandGeneratorTab;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class EffectGenerator extends CommandGenerator {
    private List<String> effectCache = new ArrayList<>();
    private boolean cacheLoaded = false;

    public EffectGenerator(CommandGeneratorTab tab) {
        super(tab);
        loadCacheAsync();
    }

    @Override public String getName() { return "EFFECT"; }

    @Override
    public String buildCommand(String target, String value, String amount) {
        if (value.isEmpty()) return "/effect clear " + (target.isEmpty() ? "@p" : target);
        
        StringBuilder cmd = new StringBuilder("/effect give ");
        cmd.append(target.isEmpty() ? "@p" : target).append(" ");
        cmd.append(value).append(" ");
        
        int duration;
        try { duration = Integer.parseInt(amount); } catch (NumberFormatException e) { duration = 30; }
        cmd.append(Math.max(1, duration)).append(" ");
        
        // Amplifier from amount field if provided differently
        cmd.append("0 true");
        
        return cmd.toString();
    }

    @Override public void tick() {
        if (!cacheLoaded && cacheFuture != null && cacheFuture.isDone()) {
            cacheLoaded = true;
        }
    }

    @Override
    public List<String> getSuggestions(String input) {
        if (!cacheLoaded) return List.of();
        
        String lower = input.toLowerCase();
        return effectCache.stream()
            .filter(s -> s.toLowerCase().contains(lower))
            .limit(20)
            .collect(Collectors.toList());
    }

    private void loadCacheAsync() {
        cacheFuture = CompletableFuture.runAsync(() -> {
            List<String> temp = new ArrayList<>();
            for (MobEffect effect : BuiltInRegistries.MOB_EFFECT) {
                ResourceLocation id = BuiltInRegistries.MOB_EFFECT.getKey(effect);
                temp.add(id.toString());
            }
            temp.sort(String::compareTo);
            this.effectCache = temp;
        });
    }
}