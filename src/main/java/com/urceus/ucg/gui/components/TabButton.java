// src/main/java/com/urceus/ucg/gui/components/TabButton.java
package com.urceus.ucg.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.urceus.ucg.UCGMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.function.Consumer;

public class TabButton extends Button {
    private final int tabIndex;
    private boolean active = false;
    private final Consumer<Integer> onClick;

    public TabButton(int x, int y, int width, int height, Component message, int tabIndex, Consumer<Integer> onClick) {
        super(x, y, width, height, message, button -> onClick.accept(tabIndex), DEFAULT_NARRATION);
        this.tabIndex = tabIndex;
        this.onClick = onClick;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Background
        int bgColor = active ? 0xFF_FFD700 : (isHoveredOrFocused() ? 0xFF_555555 : 0xFF_333333);
        guiGraphics.fill(getX(), getY(), getX() + width, getY() + height, bgColor | 0xFF000000);
        
        // Border
        int borderColor = active ? 0xFF_FFD700 : 0xFF_888888;
        guiGraphics.hLine(getX(), getX() + width - 1, getY(), borderColor);
        guiGraphics.hLine(getX(), getX() + width - 1, getY() + height - 1, borderColor);
        guiGraphics.vLine(getX(), getY(), getY() + height - 1, borderColor);
        guiGraphics.vLine(getX() + width - 1, getY(), getY() + height - 1, borderColor);
        
        // Text
        int textColor = active ? 0xFF_000000 : 0xFF_FFFFFF;
        guiGraphics.drawCenteredString(this.font, this.getMessage(), 
            getX() + width / 2, getY() + (height - 8) / 2, textColor);
    }

    public int getTabIndex() { return tabIndex; }
    public void setActive(boolean active) { this.active = active; }
    public boolean isActive() { return active; }
}