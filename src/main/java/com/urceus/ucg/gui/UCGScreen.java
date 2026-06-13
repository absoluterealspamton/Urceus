// src/main/java/com/urceus/ucg/gui/UCGScreen.java
package com.urceus.ucg.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.urceus.ucg.UCGMod;
import com.urceus.ucg.config.ClicksData;
import com.urceus.ucg.easteregg.ScareOverlay;
import com.urceus.ucg.gui.components.RotatingPotRenderer;
import com.urceus.ucg.gui.components.TabButton;
import com.urceus.ucg.gui.tabs.CommandGeneratorTab;
import com.urceus.ucg.gui.tabs.QoLTab;
import com.urceus.ucg.gui.tabs.SettingsTab;
import com.urceus.ucg.util.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class UCGScreen extends Screen {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 450;
    private static final BlockState POT_STATE = Blocks.DECORATED_POT.defaultBlockState();
    
    private final List<TabButton> tabButtons = new ArrayList<>();
    private int activeTab = 0;
    private long lastTabSwitch = 0;
    
    // Components
    private RotatingPotRenderer potRenderer;
    private Button potOverlayButton;
    private CommandGeneratorTab commandGeneratorTab;
    private QoLTab qolTab;
    private SettingsTab settingsTab;
    
    // Scare sequence
    private boolean scareTriggered = false;
    private int scareDelayTicks = 0;
    private boolean potHidden = false;
    private boolean potButtonDisabled = false;

    public UCGScreen() {
        super(Component.literal(""));
    }

    @Override
    protected void init() {
        this.potRenderer = new RotatingPotRenderer(POT_STATE);
        
        // Create tab buttons
        int tabWidth = 160;
        int tabHeight = 25;
        int startX = (this.width - WIDTH) / 2 + 20;
        int startY = (this.height - HEIGHT) / 2 + 60;
        
        tabButtons.clear();
        tabButtons.add(new TabButton(startX, startY, tabWidth, tabHeight, 
            Component.literal("Command Generator"), 0, this::setActiveTab));
        tabButtons.add(new TabButton(startX + tabWidth + 5, startY, tabWidth, tabHeight, 
            Component.literal("QoL"), 1, this::setActiveTab));
        tabButtons.add(new TabButton(startX + 2 * (tabWidth + 5), startY, tabWidth, tabHeight, 
            Component.literal("Settings"), 2, this::setActiveTab));
        
        // Pot overlay button (transparent, covers the 3D pot)
        int potX = startX - 50;
        int potY = startY - 70;
        this.potOverlayButton = Button.builder(Component.empty(), button -> {
            if (!potButtonDisabled) {
                handlePotClick();
            }
        }).bounds(potX, potY, 50, 50).build();
        
        // Initialize tabs
        this.commandGeneratorTab = new CommandGeneratorTab(this);
        this.qolTab = new QoLTab(this);
        this.settingsTab = new SettingsTab(this);
        
        // Add widgets
        tabButtons.forEach(this::addRenderableWidget);
        this.addRenderableWidget(potOverlayButton);
        
        // Show active tab
        switchTab(activeTab);
        
        // Check lock state
        if (ConfigManager.clicksData != null && ConfigManager.clicksData.isLocked() && minecraft.level != null) {
            long dayTime = minecraft.level.getDayTime();
            if (!ConfigManager.clicksData.checkLockExpired(dayTime)) {
                this.onClose();
                return;
            }
        }
    }

    private void setActiveTab(int tab) {
        if (System.currentTimeMillis() - lastTabSwitch < 200) return; // Debounce
        lastTabSwitch = System.currentTimeMillis();
        switchTab(tab);
    }

    private void switchTab(int tab) {
        activeTab = tab;
        // Clear dynamic widgets except tabs and pot button
        this.children().removeIf(w -> !(w instanceof TabButton) && w != potOverlayButton);
        
        tabButtons.forEach(b -> b.setActive(b.getTabIndex() == tab));
        
        switch (tab) {
            case 0 -> commandGeneratorTab.init(minecraft, width, height);
            case 1 -> qolTab.init(minecraft, width, height);
            case 2 -> settingsTab.init(minecraft, width, height);
        }
    }

    private void handlePotClick() {
        if (ConfigManager.clicksData == null) return;
        
        ConfigManager.clicksData.incrementClick();
        EasterEggManager.sendPhraseToChat();
        
        if (ConfigManager.clicksData.getClickCount() == 100) {
            triggerScareSequence();
        }
    }

    public void triggerScareSequence() {
        this.scareTriggered = true;
        this.potHidden = true;
        this.potButtonDisabled = true;
        this.scareDelayTicks = 60 + ThreadLocalRandom.current().nextInt(141); // 60-200 ticks
        
        if (ConfigManager.clicksData != null && minecraft.level != null) {
            ConfigManager.clicksData.triggerLock(minecraft.level.getDayTime());
        }
    }

    @Override
    public void tick() {
        super.tick();
        
        if (scareTriggered && scareDelayTicks > 0) {
            scareDelayTicks--;
            if (scareDelayTicks == 0) {
                ScareOverlay.trigger(() -> {
                    minecraft.execute(() -> {
                        if (minecraft.screen == this) {
                            minecraft.setScreen(null);
                        }
                    });
                });
            }
        }
        
        // Update active tab
        switch (activeTab) {
            case 0 -> commandGeneratorTab.tick();
            case 1 -> qolTab.tick();
            case 2 -> settingsTab.tick();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Render background
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        
        // Calculate positions
        int left = (this.width - WIDTH) / 2;
        int top = (this.height - HEIGHT) / 2;
        
        // Render main panel background
        guiGraphics.fillGradient(left, top, left + WIDTH, top + HEIGHT, 
            0x80000000, 0xA0000000);
        
        // Render title with split colors
        renderTitle(guiGraphics, left, top);
        
        // Render rotating 3D pot
        if (!potHidden) {
            renderPot(guiGraphics, left, top, partialTick);
        }
        
        // Render tab bar background
        int tabY = top + 55;
        guiGraphics.fill(left + 10, tabY, left + WIDTH - 10, tabY + 35, 0x60000000);
        
        // Render widgets (tabs, pot button, active tab content)
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        
        // Render scare overlay if active
        if (ScareOverlay.isActive()) {
            ScareOverlay.render(guiGraphics, this.width, this.height);
        }
        
        // Render tooltips
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void renderTitle(GuiGraphics guiGraphics, int left, int top) {
        int titleX = left + 20;
        int titleY = top + 15;
        
        // "[Urceus] " in GOLD + BOLD
        MutableComponent urceusPart = Component.literal("[Urceus] ")
            .withStyle(Style.EMPTY.withColor(0xFFD700).withBold(true));
        // "Command Generator" in WHITE
        MutableComponent generatorPart = Component.literal("Command Generator")
            .withStyle(Style.EMPTY.withColor(0xFFFFFF));
        
        // Render first part
        guiGraphics.drawString(this.font, urceusPart, titleX, titleY, 0xFFFFFF);
        
        // Render second part after first
        int urceusWidth = this.font.width("[Urceus] ");
        guiGraphics.drawString(this.font, generatorPart, titleX + urceusWidth, titleY, 0xFFFFFF);
    }

    private void renderPot(GuiGraphics guiGraphics, int left, int top, float partialTick) {
        int potX = left - 30;
        int potY = top - 10;
        
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(potX + 25, potY + 25, 100);
        guiGraphics.pose().mulPose(new Quaternionf().rotateY((float) (Minecraft.getInstance().level.getGameTime() * 0.01)));
        guiGraphics.pose().scale(30, 30, 30);
        guiGraphics.pose().translate(-0.5, -0.5, -0.5);
        
        potRenderer.render(guiGraphics);
        
        guiGraphics.pose().popPose();
    }

    public boolean isPotButtonClicked(double mouseX, double mouseY) {
        return potOverlayButton.isMouseOver(mouseX, mouseY);
    }

    @Override
    public boolean isPauseScreen() {
        return false; // CRITICAL: Must not pause the game
    }

    @Override
    public void onClose() {
        if (scareTriggered) return; // Don't allow close during scare
        super.onClose();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    // Getters for tabs
    public CommandGeneratorTab getCommandGeneratorTab() { return commandGeneratorTab; }
    public QoLTab getQoLTab() { return qolTab; }
    public SettingsTab getSettingsTab() { return settingsTab; }
    public int getActiveTab() { return activeTab; }
}