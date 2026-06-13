// src/main/java/com/urceus/ucg/gui/generators/AttributeGenerator.java
package com.urceus.ucg.gui.generators;

import com.urceus.ucg.gui.tabs.CommandGeneratorTab;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AttributeGenerator extends CommandGenerator {
    private List<String> attributeCache = new ArrayList<>();
    private boolean cacheLoaded = false;

    public AttributeGenerator(CommandGeneratorTab tab) { super(tab); loadCacheAsync(); }

    @Override public String getName() { return "ATTRIBUTE"; }

    @Override
    public String buildCommand(String target, String value, String amount) {
        if (value.isEmpty()) return "";
        return "/attribute " + (target.isEmpty() ? "@p" : target) + " " + value + " base set " + (amount.isEmpty() ? "1" : amount);
    }

    @Override public void tick() {
        if (!cacheLoaded && cacheFuture != null && cacheFuture.isDone()) cacheLoaded = true;
    }

    @Override public List<String> getSuggestions(String input) {
        if (!cacheLoaded) return List.of();
        String lower = input.toLowerCase();
        return attributeCache.stream().filter(s -> s.toLowerCase().contains(lower)).limit(20).collect(Collectors.toList());
    }

    private void loadCacheAsync() {
        cacheFuture = CompletableFuture.runAsync(() -> {
            List<String> temp = new ArrayList<>();
            for (Attribute attr : BuiltInRegistries.ATTRIBUTE) {
                temp.add(BuiltInRegistries.ATTRIBUTE.getKey(attr).toString());
            }
            temp.sort(String::compareTo);
            this.attributeCache = temp;
        });
    }
}