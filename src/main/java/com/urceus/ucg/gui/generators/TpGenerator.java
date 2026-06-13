// src/main/java/com/urceus/ucg/gui/generators/TpGenerator.java
package com.urceus.ucg.gui.generators;

import com.urceus.ucg.gui.tabs.CommandGeneratorTab;

public class TpGenerator extends CommandGenerator {
    public TpGenerator(CommandGeneratorTab tab) { super(tab); }

    @Override public String getName() { return "TP"; }

    @Override
    public String buildCommand(String target, String value, String amount) {
        StringBuilder cmd = new StringBuilder("/tp ");
        
        if (!target.isEmpty()) {
            cmd.append(target).append(" ");
        }
        
        if (!value.isEmpty()) {
            cmd.append(value);
        } else {
            cmd.append("@p");
        }
        
        return cmd.toString();
    }

    @Override public void tick() {}
    @Override public List<String> getSuggestions(String input) { return List.of("@p", "@a", "@r", "@e", "@s"); }
}