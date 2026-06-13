// src/main/java/com/urceus/ucg/gui/generators/DataGenerator.java
package com.urceus.ucg.gui.generators;

import com.urceus.ucg.gui.tabs.CommandGeneratorTab;

public class DataGenerator extends CommandGenerator {
    public DataGenerator(CommandGeneratorTab tab) { super(tab); }

    @Override public String getName() { return "DATA"; }

    @Override
    public String buildCommand(String target, String value, String amount) {
        if (value.isEmpty()) return "";
        String action = amount.isEmpty() ? "get" : amount; // get, merge, modify, remove
        return "/data " + action + " " + (target.isEmpty() ? "block ~ ~ ~" : target) + " " + value;
    }

    @Override public void tick() {}
    @Override public List<String> getSuggestions(String input) { return List.of("get", "merge", "modify", "remove", "block ~ ~ ~", "entity @p", "storage minecraft:test"); }
}