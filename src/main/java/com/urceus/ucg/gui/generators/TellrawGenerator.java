// src/main/java/com/urceus/ucg/gui/generators/TellrawGenerator.java
package com.urceus.ucg.gui.generators;

import com.urceus.ucg.gui.tabs.CommandGeneratorTab;

public class TellrawGenerator extends CommandGenerator {
    public TellrawGenerator(CommandGeneratorTab tab) { super(tab); }

    @Override public String getName() { return "TELLRAW"; }

    @Override
    public String buildCommand(String target, String value, String amount) {
        if (value.isEmpty()) return "";
        return "/tellraw " + (target.isEmpty() ? "@a" : target) + " " + value;
    }

    @Override public void tick() {}
    @Override public List<String> getSuggestions(String input) { return List.of("{\"text\":\"Hello\"}", "{\"text\":\"World\",\"color\":\"gold\"}"); }
}