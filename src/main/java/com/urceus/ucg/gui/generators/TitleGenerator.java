// src/main/java/com/urceus/ucg/gui/generators/TitleGenerator.java
package com.urceus.ucg.gui.generators;

import com.urceus.ucg.gui.tabs.CommandGeneratorTab;

public class TitleGenerator extends CommandGenerator {
    public TitleGenerator(CommandGeneratorTab tab) { super(tab); }

    @Override public String getName() { return "TITLE"; }

    @Override
    public String buildCommand(String target, String value, String amount) {
        if (value.isEmpty()) return "";
        String action = amount.isEmpty() ? "title" : amount; // title, subtitle, actionbar, clear, reset
        return "/title " + (target.isEmpty() ? "@a" : target) + " " + action + " " + value;
    }

    @Override public void tick() {}
    @Override public List<String> getSuggestions(String input) { return List.of("title", "subtitle", "actionbar", "clear", "reset"); }
}