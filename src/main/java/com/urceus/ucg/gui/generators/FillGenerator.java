// src/main/java/com/urceus/ucg/gui/generators/FillGenerator.java
package com.urceus.ucg.gui.generators;

import com.urceus.ucg.gui.tabs.CommandGeneratorTab;

public class FillGenerator extends CommandGenerator {
    public FillGenerator(CommandGeneratorTab tab) { super(tab); }

    @Override public String getName() { return "FILL"; }

    @Override
    public String buildCommand(String target, String value, String amount) {
        if (value.isEmpty()) return "";
        String[] coords = target.split("\\s+");
        String from = coords.length >= 3 ? coords[0] + " " + coords[1] + " " + coords[2] : "~ ~ ~";
        String to = coords.length >= 6 ? coords[3] + " " + coords[4] + " " + coords[5] : "~10 ~10 ~10";
        return "/fill " + from + " " + to + " " + value + (amount.isEmpty() ? "" : " " + amount);
    }

    @Override public void tick() {}
    @Override public List<String> getSuggestions(String input) { return List.of("replace", "destroy", "keep", "hollow", "outline"); }
}