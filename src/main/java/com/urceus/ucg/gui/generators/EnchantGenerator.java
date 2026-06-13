// src/main/java/com/urceus/ucg/gui/generators/EnchantGenerator.java
package com.urceus.ucg.gui.generators;

import com.urceus.ucg.gui.tabs.CommandGeneratorTab;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class EnchantGenerator extends CommandGenerator {
    private List<String> enchantCache = new ArrayList<>();
    private boolean cacheLoaded = false;

    public EnchantGenerator(CommandGeneratorTab tab) {
        super(tab);
        loadCacheAsync();
    }

    @Override public String getName() { return "ENCHANT"; }

    @Override
    public String buildCommand(String target, String value, String amount) {
        if (value.isEmpty()) return "";
        
        StringBuilder cmd = new StringBuilder("/enchant ");
        cmd.append(target.isEmpty() ? "@p" : target).append(" ");
        cmd.append(value).append(" ");
        
        int level;
        try { level = Integer.parseInt(amount); } catch (NumberFormatException e) { level = 1; }
        cmd.append(Math.max(1, level));
        
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
        return enchantCache.stream()
            .filter(s -> s.toLowerCase().contains(lower))
            .limit(20)
            .collect(Collectors.toList());
    }

    private void loadCacheAsync() {
        cacheFuture = CompletableFuture.runAsync(() -> {
            List<String> temp = new ArrayList<>();
            for (Enchantment ench : BuiltInRegistries.ENCHANTMENT) {
                ResourceLocation id = BuiltInRegistries.ENCHANTMENT.getKey(ench);
                temp.add(id.toString());
            }
            temp.sort(String::compareTo);
            this.enchantCache = temp;
        });
    }
}