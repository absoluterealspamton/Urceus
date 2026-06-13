// src/main/java/com/urceus/ucg/gui/tabs/CommandGeneratorTab.java
package com.urceus.ucg.gui.tabs;

import com.urceus.ucg.UCGMod;
import com.urceus.ucg.gui.UCGScreen;
import com.urceus.ucg.gui.components.FuzzySearchTextField;
import com.urceus.ucg.gui.components.LiveCommandPreview;
import com.urceus.ucg.gui.generators.*;
import com.urceus.ucg.util.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.ChatFormatting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CommandGeneratorTab {
    private final UCGScreen parent;
    private final List<CommandGenerator> generators = new ArrayList<>();
    private CommandGenerator activeGenerator;
    private Button[] generatorButtons = new Button[13];
    
    // UI Components
    private EditBox targetField;
    private EditBox valueField;
    private EditBox amountField;
    private FuzzySearchTextField searchField;
    private LiveCommandPreview preview;
    private Button executeButton;
    private Button saveButton;
    
    private int selectedIndex = 0;

    public CommandGeneratorTab(UCGScreen parent) {
        this.parent = parent;
        initializeGenerators();
    }

    private void initializeGenerators() {
        generators.add(new GiveGenerator(this));
        generators.add(new SummonGenerator(this));
        generators.add(new TpGenerator(this));
        generators.add(new EnchantGenerator(this));
        generators.add(new EffectGenerator(this));
        generators.add(new ParticleGenerator(this));
        generators.add(new TellrawGenerator(this));
        generators.add(new TitleGenerator(this));
        generators.add(new AttributeGenerator(this));
        generators.add(new DataGenerator(this));
        generators.add(new SetblockGenerator(this));
        generators.add(new FillGenerator(this));
        generators.add(new TimeGenerator(this));
        
        activeGenerator = generators.get(0);
    }

    public void init(Minecraft mc, int width, int height) {
        int left = (width - 600) / 2;
        int top = (height - 450) / 2;
        int contentTop = top + 100;
        int contentLeft = left + 20;
        
        // Generator selection buttons (13 buttons in grid)
        String[] labels = {"GIVE", "SUMMON", "TP", "ENCHANT", "EFFECT", "PARTICLE", 
                          "TELLRAW", "TITLE", "ATTRIBUTE", "DATA", "SETBLOCK", "FILL", "TIME"};
        
        for (int i = 0; i < 13; i++) {
            int col = i % 7;
            int row = i / 7;
            int x = contentLeft + col * 80;
            int y = contentTop + row * 35;
            
            final int index = i;
            generatorButtons[i] = Button.builder(Component.literal(labels[i]), btn -> selectGenerator(index))
                .bounds(x, y, 75, 25)
                .tooltip(Tooltip.create(Component.literal("Select " + labels[i] + " generator")))
                .build();
            parent.addRenderableWidget(generatorButtons[i]);
        }
        
        // Target field
        targetField = new EditBox(mc.font, contentLeft, contentTop + 80, 180, 20, Component.literal("Target"));
        targetField.setValue(ConfigManager.clicksData != null ? "@p" : "@p");
        targetField.setBordered(true);
        targetField.setMaxLength(100);
        targetField.setResponder(s -> updatePreview());
        parent.addRenderableWidget(targetField);
        
        // Value field (with fuzzy search for /give)
        valueField = new EditBox(mc.font, contentLeft + 200, contentTop + 80, 180, 20, Component.literal("Value"));
        valueField.setBordered(true);
        valueField.setMaxLength(200);
        valueField.setResponder(s -> updatePreview());
        parent.addRenderableWidget(valueField);
        
        // Fuzzy search overlay for /give
        searchField = new FuzzySearchTextField(mc.font, contentLeft + 200, contentTop + 80, 180, 20, Component.literal("Search items..."));
        searchField.setVisible(false);
        searchField.setResponder(s -> {
            valueField.setValue(s);
            updatePreview();
        });
        parent.addRenderableWidget(searchField);
        
        // Amount field
        amountField = new EditBox(mc.font, contentLeft + 400, contentTop + 80, 80, 20, Component.literal("Amount"));
        amountField.setValue("1");
        amountField.setBordered(true);
        amountField.setMaxLength(10);
        amountField.setResponder(s -> updatePreview());
        parent.addRenderableWidget(amountField);
        
        // Execute button
        executeButton = Button.builder(Component.literal("Execute"), btn -> executeCommand())
            .bounds(contentLeft, contentTop + 115, 100, 25)
            .build();
        parent.addRenderableWidget(executeButton);
        
        // Save button
        saveButton = Button.builder(Component.literal("Save"), btn -> saveCommand())
            .bounds(contentLeft + 110, contentTop + 115, 100, 25)
            .build();
        parent.addRenderableWidget(saveButton);
        
        // Live preview
        preview = new LiveCommandPreview(mc.font, contentLeft, contentTop + 150, 560, 200);
        updatePreview();
        
        // Update button states
        updateButtonStates();
    }

    private void selectGenerator(int index) {
        selectedIndex = index;
        activeGenerator = generators.get(index);
        updateButtonStates();
        updatePreview();
        
        // Show/hide fuzzy search for /give
        boolean isGive = activeGenerator instanceof GiveGenerator;
        searchField.setVisible(isGive);
        valueField.setVisible(!isGive || searchField.getValue().isEmpty());
    }

    private void updateButtonStates() {
        for (int i = 0; i < generatorButtons.length; i++) {
            generatorButtons[i].active = (i != selectedIndex);
        }
    }

    private void updatePreview() {
        if (activeGenerator != null) {
            String command = activeGenerator.buildCommand(
                targetField.getValue(),
                valueField.getValue(),
                amountField.getValue()
            );
            preview.setCommand(command);
        }
    }

    private void executeCommand() {
        if (activeGenerator == null) return;
        
        String command = activeGenerator.buildCommand(
            targetField.getValue(),
            valueField.getValue(),
            amountField.getValue()
        );
        
        // Add to history
        if (ConfigManager.historyData != null) {
            ConfigManager.historyData.addCommand(command);
        }
        
        // Send to chat
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.connection.sendCommand(command);
        }
    }

    private void saveCommand() {
        if (activeGenerator == null) return;
        
        String command = activeGenerator.buildCommand(
            targetField.getValue(),
            valueField.getValue(),
            amountField.getValue()
        );
        
        String name = activeGenerator.getName() + "_" + System.currentTimeMillis();
        if (ConfigManager.savedCommandsData != null) {
            ConfigManager.savedCommandsData.saveCommand(name, command, activeGenerator.getName());
        }
    }

    public void tick() {
        if (activeGenerator != null) {
            activeGenerator.tick();
        }
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        preview.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    // Getters
    public EditBox getTargetField() { return targetField; }
    public EditBox getValueField() { return valueField; }
    public EditBox getAmountField() { return amountField; }
    public FuzzySearchTextField getSearchField() { return searchField; }
    public CommandGenerator getActiveGenerator() { return activeGenerator; }
}