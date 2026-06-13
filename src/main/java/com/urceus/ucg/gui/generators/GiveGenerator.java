// src/main/java/com/urceus/ucg/gui/generators/GiveGenerator.java
package com.urceus.ucg.gui.generators;

import com.urceus.ucg.gui.tabs.CommandGeneratorTab;
import com.urceus.ucg.util.CommandUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class GiveGenerator extends CommandGenerator {
    private List<ItemSuggestion> itemCache = new ArrayList<>();
    private boolean cacheLoaded = false;

    public GiveGenerator(CommandGeneratorTab tab) {
        super(tab);
        loadCacheAsync();
    }

    @Override
    public String getName() { return "GIVE"; }

    @Override
    public String buildCommand(String target, String value, String amount) {
        if (value.isEmpty()) return "";
        
        StringBuilder cmd = new StringBuilder("/give ");
        cmd.append(target.isEmpty() ? "@p" : target).append(" ");
        cmd.append(value).append(" ");
        
        int amt;
        try { amt = Integer.parseInt(amount); } catch (NumberFormatException e) { amt = 1; }
        cmd.append(Math.max(1, Math.min(amt, 64)));
        
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
        return itemCache.stream()
            .filter(s -> s.name.toLowerCase().contains(lower) || s.id.toLowerCase().contains(lower))
            .limit(20)
            .map(s -> s.id)
            .collect(Collectors.toList());
    }

    public List<ItemSuggestion> getItemSuggestions(String input) {
        if (!cacheLoaded) return List.of();
        
        String lower = input.toLowerCase();
        return itemCache.stream()
            .filter(s -> s.name.toLowerCase().contains(lower) || s.id.toLowerCase().contains(lower))
            .limit(20)
            .collect(Collectors.toList());
    }

    private void loadCacheAsync() {
        cacheFuture = CompletableFuture.runAsync(() -> {
            List<ItemSuggestion> temp = new ArrayList<>();
            for (Item item : BuiltInRegistries.ITEM) {
                ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
                String name = item.getDescriptionId();
                temp.add(new ItemSuggestion(id.toString(), name));
            }
            // Sort by name
            temp.sort(Comparator.comparing(s -> s.name));
            this.itemCache = temp;
        });
    }

    public static class ItemSuggestion {
        public final String id;
        public final String name;

        public ItemSuggestion(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}