// src/main/java/com/urceus/ucg/gui/generators/TimeGenerator.java
package com.urceus.ucg.gui.generators;

import com.urceus.ucg.gui.tabs.CommandGeneratorTab;

public class TimeGenerator extends CommandGenerator {
    public TimeGenerator(CommandGeneratorTab tab) { super(tab); }

    @Override public String getName() { return "TIME"; }

    @Override
    public String buildCommand(String target, String value, String amount) {
        String sub = value.isEmpty() ? "set" : value; // set, add, query
        String val = amount.isEmpty() ? "day" : amount; // day, night, noon, midnight, <ticks>
        return "/time " + sub + " " + val;
    }

    @Override public void tick() {}
    @Override public List<String> getSuggestions(String input) { return List.of("set", "add", "query", "day", "night", "noon", "midnight", "1000", "6000", "12000", "18000"); }
}