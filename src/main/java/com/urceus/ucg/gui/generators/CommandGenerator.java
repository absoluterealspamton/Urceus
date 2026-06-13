// src/main/java/com/urceus/ucg/gui/generators/CommandGenerator.java
package com.urceus.ucg.gui.generators;

import com.urceus.ucg.gui.tabs.CommandGeneratorTab;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.EntityType;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public abstract class CommandGenerator {
    protected final CommandGeneratorTab tab;
    protected List<String> cachedSuggestions = List.of();
    protected CompletableFuture<Void> cacheFuture;

    public CommandGenerator(CommandGeneratorTab tab) {
        this.tab = tab;
    }

    public abstract String getName();
    public abstract String buildCommand(String target, String value, String amount);
    public abstract void tick();
    public abstract List<String> getSuggestions(String input);
    
    public void loadSuggestionsAsync() {
        // Override in subclasses
    }
    
    protected List<String> getItemSuggestions(String input) {
        String lower = input.toLowerCase();
        return BuiltInRegistries.ITEM.stream()
            .map(Item::getDescriptionId)
            .filter(id -> id.toLowerCase().contains(lower))
            .map(id -> ResourceLocation.parse(id.replace("item.", "")).toString())
            .limit(50)
            .collect(Collectors.toList());
    }
    
    protected List<String> getEntitySuggestions(String input) {
        String lower = input.toLowerCase();
        return BuiltInRegistries.ENTITY_TYPE.stream()
            .map(EntityType::getDescriptionId)
            .filter(id -> id.toLowerCase().contains(lower))
            .map(id -> ResourceLocation.parse(id.replace("entity.", "")).toString())
            .limit(50)
            .collect(Collectors.toList());
    }
}