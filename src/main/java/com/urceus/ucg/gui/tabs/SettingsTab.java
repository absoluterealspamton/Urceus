// src/main/java/com/urceus/ucg/gui/tabs/SettingsTab.java
package com.urceus.ucg.gui.tabs;

import com.urceus.ucg.UCGMod;
import com.urceus.ucg.config.UCGConfig;
import com.urceus.ucg.gui.UCGScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public class SettingsTab {
    private final UCGScreen parent;
    private EditBox defaultTargetField;

    public SettingsTab(UCGScreen parent) { this.parent = parent; }

    public void init(Minecraft mc, int width, int height) {
        int left = (width - 600) / 2;
        int top = (height - 450) / 2;
        int contentTop = top + 100;
        int contentLeft = left + 20;

        // Default Target
        defaultTargetField = new EditBox(mc.font, contentLeft, contentTop, 200, 20, 
            Component.literal("Default Target"));
        defaultTargetField.setValue(UCGConfig.CLIENT.defaultTarget.get());
        defaultTargetField.setBordered(true);
        defaultTargetField.setMaxLength(50);
        defaultTargetField.setResponder(s -> UCGConfig.CLIENT.defaultTarget.set(s));
        parent.addRenderableWidget(defaultTargetField);

        // Auto Creative Toggle
        Button autoCreativeBtn = Button.builder(
            Component.literal("Auto Creative: " + (UCGConfig.CLIENT.autoCreative.get() ? "ON" : "OFF"))
                .withStyle(UCGConfig.CLIENT.autoCreative.get() ? ChatFormatting.GREEN : ChatFormatting.RED),
            btn -> {
                boolean newVal = !UCGConfig.CLIENT.autoCreative.get();
                UCGConfig.CLIENT.autoCreative.set(newVal);
                btn.setMessage(Component.literal("Auto Creative: " + (newVal ? "ON" : "OFF"))
                    .withStyle(newVal ? ChatFormatting.GREEN : ChatFormatting.RED));
            }
        ).bounds(contentLeft, contentTop + 35, 250, 25).build();
        parent.addRenderableWidget(autoCreativeBtn);

        // Enable Urceus's Wrath Toggle
        Button wrathBtn = Button.builder(
            Component.literal("Enable Urceus's Wrath: " + (UCGConfig.CLIENT.enableUrceusWrath.get() ? "ON" : "OFF"))
                .withStyle(UCGConfig.CLIENT.enableUrceusWrath.get() ? ChatFormatting.GREEN : ChatFormatting.RED),
            btn -> {
                boolean newVal = !UCGConfig.CLIENT.enableUrceusWrath.get();
                UCGConfig.CLIENT.enableUrceusWrath.set(newVal);
                btn.setMessage(Component.literal("Enable Urceus's Wrath: " + (newVal ? "ON" : "OFF"))
                    .withStyle(newVal ? ChatFormatting.GREEN : ChatFormatting.RED));
            }
        ).bounds(contentLeft, contentTop + 70, 300, 25).build();
        parent.addRenderableWidget(wrathBtn);

        // Reset Click Counter
        Button resetClicksBtn = Button.builder(
            Component.literal("Reset Click Counter")
                .withStyle(ChatFormatting.RED),
            btn -> resetClicks()
        ).bounds(contentLeft, contentTop + 105, 200, 25).build();
        parent.addRenderableWidget(resetClicksBtn);
    }

    private void resetClicks() {
        if (com.urceus.ucg.util.ConfigManager.clicksData != null) {
            com.urceus.ucg.util.ConfigManager.clicksData.setClickCount(0);
            com.urceus.ucg.util.ConfigManager.clicksData.setLocked(false);
            com.urceus.ucg.util.ConfigManager.clicksData.saveAsync();
        }
    }

    public void tick() {}

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {}
}