// src/main/java/com/urceus/ucg/gui/components/LiveCommandPreview.java
package com.urceus.ucg.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.ChatFormatting;

import java.util.List;

public class LiveCommandPreview {
    private final Font font;
    private final int x, y, width, height;
    private String command = "";
    private List<Component> wrappedLines = List.of();

    public LiveCommandPreview(Font font, int x, int y, int width, int height) {
        this.font = font;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setCommand(String command) {
        this.command = command;
        wrapText();
    }

    private void wrapText() {
        if (command.isEmpty()) {
            wrappedLines = List.of(Component.literal("No command generated").withStyle(ChatFormatting.GRAY));
            return;
        }
        
        // Split by spaces and wrap
        wrappedLines = font.splitLines(Component.literal(command).withStyle(ChatFormatting.GREEN), width);
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Background
        guiGraphics.fill(x - 5, y - 5, x + width + 5, y + height + 5, 0x80_000000);
        guiGraphics.hLine(x - 5, x + width + 4, y - 5, 0xFF_444444);
        guiGraphics.hLine(x - 5, x + width + 4, y + height + 4, 0xFF_444444);
        guiGraphics.vLine(x - 5, y - 5, y + height + 4, 0xFF_444444);
        guiGraphics.vLine(x + width + 4, y - 5, y + height + 4, 0xFF_444444);
        
        // Label
        guiGraphics.drawString(font, Component.literal("Live Preview:").withStyle(ChatFormatting.GOLD), 
            x, y - 15, 0xFFFFFFFF);
        
        // Content
        int lineHeight = 12;
        int maxLines = height / lineHeight;
        
        for (int i = 0; i < Math.min(wrappedLines.size(), maxLines); i++) {
            guiGraphics.drawString(font, wrappedLines.get(i), x, y + i * lineHeight, 0xFFFFFFFF);
        }
        
        // Copy hint
        if (!command.isEmpty()) {
            String hint = "Click to copy to clipboard";
            int hintWidth = font.width(hint);
            guiGraphics.drawString(font, Component.literal(hint).withStyle(ChatFormatting.GRAY), 
                x + width - hintWidth, y + height + 8, 0xFF_888888);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY) {
        if (!command.isEmpty() && 
            mouseX >= x && mouseX <= x + width &&
            mouseY >= y && mouseY <= y + height) {
            Minecraft.getInstance().keyboardHandler.setClipboard(command);
            return true;
        }
        return false;
    }

    public String getCommand() { return command; }
}