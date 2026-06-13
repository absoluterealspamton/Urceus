// src/main/java/com/urceus/ucg/gui/tabs/QoLTab.java
package com.urceus.ucg.gui.tabs;

import com.urceus.ucg.UCGMod;
import com.urceus.ucg.gui.UCGScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public class QoLTab {
    private final UCGScreen parent;

    public QoLTab(UCGScreen parent) { this.parent = parent; }

    public void init(Minecraft mc, int width, int height) {
        int left = (width - 600) / 2;
        int top = (height - 450) / 2;
        int contentTop = top + 100;
        int contentLeft = left + 20;

        // Item Copy Info
        Button itemCopyInfo = Button.builder(
            Component.literal("Item Copy: Ctrl + Middle Click on inventory slot")
                .withStyle(ChatFormatting.GREEN), 
            btn -> {}
        ).bounds(contentLeft, contentTop, 500, 25).build();
        itemCopyInfo.active = false;
        parent.addRenderableWidget(itemCopyInfo);

        // Entity Copy Info
        Button entityCopyInfo = Button.builder(
            Component.literal("Entity Copy: Ctrl + Middle Click on living entity")
                .withStyle(ChatFormatting.GREEN), 
            btn -> {}
        ).bounds(contentLeft, contentTop + 30, 500, 25).build();
        entityCopyInfo.active = false;
        parent.addRenderableWidget(entityCopyInfo);

        // History Button
        Button historyButton = Button.builder(
            Component.literal("View Command History (Last 10)"),
            btn -> showHistory()
        ).bounds(contentLeft, contentTop + 70, 250, 25).build();
        parent.addRenderableWidget(historyButton);

        // Clear History Button
        Button clearHistoryButton = Button.builder(
            Component.literal("Clear History"),
            btn -> clearHistory()
        ).bounds(contentLeft + 260, contentTop + 70, 120, 25)
        .withStyle(ChatFormatting.RED)
        .build();
        parent.addRenderableWidget(clearHistoryButton);
    }

    private void showHistory() {
        // Implementation would show a popup with history
    }

    private void clearHistory() {
        if (com.urceus.ucg.util.ConfigManager.historyData != null) {
            com.urceus.ucg.util.ConfigManager.historyData.getHistory().clear();
            com.urceus.ucg.util.ConfigManager.historyData.saveAsync();
        }
    }

    public void tick() {}

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Render QoL info
    }
}