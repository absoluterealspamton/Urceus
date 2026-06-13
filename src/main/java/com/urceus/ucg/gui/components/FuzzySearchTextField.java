// src/main/java/com/urceus/ucg/gui/components/FuzzySearchTextField.java
package com.urceus.ucg.gui.components;

import com.mojang.blaze3d.platform.InputConstants;
import com.urceus.ucg.gui.generators.GiveGenerator;
import com.urceus.ucg.gui.tabs.CommandGeneratorTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.function.Consumer;

public class FuzzySearchTextField extends EditBox {
    private final CommandGeneratorTab tab;
    private List<GiveGenerator.ItemSuggestion> suggestions = List.of();
    private int selectedIndex = -1;
    private int scrollOffset = 0;
    private static final int MAX_VISIBLE = 10;
    private static final int ITEM_HEIGHT = 18;

    public FuzzySearchTextField(Font font, int x, int y, int width, int height, Component message) {
        super(font, x, y, width, height, message);
        this.tab = null; // Will be set via setter
        this.setBordered(true);
        this.setMaxLength(100);
    }

    public void setTab(CommandGeneratorTab tab) {
        this.tab = tab;
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        updateSuggestions();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.isVisible() || !this.isFocused()) return false;
        
        if (keyCode == InputConstants.KEY_DOWN) {
            if (!suggestions.isEmpty()) {
                selectedIndex = Math.min(selectedIndex + 1, suggestions.size() - 1);
                adjustScroll();
                return true;
            }
        } else if (keyCode == InputConstants.KEY_UP) {
            if (!suggestions.isEmpty()) {
                selectedIndex = Math.max(selectedIndex - 1, 0);
                adjustScroll();
                return true;
            }
        } else if (keyCode == InputConstants.KEY_ENTER || keyCode == InputConstants.KEY_TAB) {
            if (selectedIndex >= 0 && selectedIndex < suggestions.size()) {
                applySuggestion(selectedIndex);
                return true;
            }
        } else if (keyCode == InputConstants.KEY_ESCAPE) {
            suggestions = List.of();
            selectedIndex = -1;
            return true;
        }
        
        boolean result = super.keyPressed(keyCode, scanCode, modifiers);
        if (result) updateSuggestions();
        return result;
    }

    private void updateSuggestions() {
        if (tab != null && tab.getActiveGenerator() instanceof GiveGenerator giveGen) {
            suggestions = giveGen.getItemSuggestions(this.getValue());
            selectedIndex = suggestions.isEmpty() ? -1 : 0;
            scrollOffset = 0;
        }
    }

    private void adjustScroll() {
        if (selectedIndex < scrollOffset) {
            scrollOffset = selectedIndex;
        } else if (selectedIndex >= scrollOffset + MAX_VISIBLE) {
            scrollOffset = selectedIndex - MAX_VISIBLE + 1;
        }
    }

    private void applySuggestion(int index) {
        if (index >= 0 && index < suggestions.size()) {
            String id = suggestions.get(index).id;
            this.setValue(id);
            if (this.getResponder() != null) {
                this.getResponder().accept(id);
            }
            suggestions = List.of();
            selectedIndex = -1;
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        
        if (!suggestions.isEmpty() && this.isFocused()) {
            renderSuggestions(guiGraphics, mouseX, mouseY);
        }
    }

    private void renderSuggestions(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int x = this.getX();
        int y = this.getY() + this.getHeight() + 2;
        int width = this.getWidth();
        int visibleCount = Math.min(MAX_VISIBLE, suggestions.size() - scrollOffset);
        int height = visibleCount * ITEM_HEIGHT;
        
        // Background
        guiGraphics.fill(x - 1, y - 1, x + width + 1, y + height + 1, 0xFF_000000);
        guiGraphics.fill(x, y, x + width, y + height, 0xFF_222222);
        
        // Items
        for (int i = 0; i < visibleCount; i++) {
            int index = scrollOffset + i;
            int itemY = y + i * ITEM_HEIGHT;
            
            if (index == selectedIndex) {
                guiGraphics.fill(x, itemY, x + width, itemY + ITEM_HEIGHT, 0xFF_4444AA);
            }
            
            GiveGenerator.ItemSuggestion suggestion = suggestions.get(index);
            guiGraphics.drawString(this.font, suggestion.id, x + 4, itemY + 3, 0xFFFFFFFF);
            guiGraphics.drawString(this.font, suggestion.name, x + 4, itemY + 12, 0xFF_AAAAAA);
        }
        
        // Scroll indicator
        if (suggestions.size() > MAX_VISIBLE) {
            float scrollRatio = (float) scrollOffset / (suggestions.size() - MAX_VISIBLE);
            int thumbHeight = Math.max(20, (int) (height * (MAX_VISIBLE / (float) suggestions.size())));
            int thumbY = y + (int) ((height - thumbHeight) * scrollRatio);
            guiGraphics.fill(x + width - 4, thumbY, x + width - 2, thumbY + thumbHeight, 0xFF_888888);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.isVisible() || !suggestions.isEmpty()) {
            int x = this.getX();
            int y = this.getY() + this.getHeight() + 2;
            int width = this.getWidth();
            int visibleCount = Math.min(MAX_VISIBLE, suggestions.size() - scrollOffset);
            int height = visibleCount * ITEM_HEIGHT;
            
            if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
                int index = (int) ((mouseY - y) / ITEM_HEIGHT) + scrollOffset;
                if (index >= 0 && index < suggestions.size()) {
                    applySuggestion(index);
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (!suggestions.isEmpty() && this.isFocused()) {
            int maxScroll = Math.max(0, suggestions.size() - MAX_VISIBLE);
            scrollOffset = Mth.clamp(scrollOffset - (int) deltaY, 0, maxScroll);
            return true;
        }
        return false;
    }
}