// src/main/java/com/urceus/ucg/events/ClickTracker.java
package com.urceus.ucg.events;

import com.urceus.ucg.UCGMod;
import com.urceus.ucg.config.ClicksData;
import com.urceus.ucg.easteregg.EasterEggManager;
import com.urceus.ucg.easteregg.ScareOverlay;
import com.urceus.ucg.util.ConfigManager;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = UCGMod.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClickTracker {
    @SubscribeEvent
    public static void onMouseClick(InputEvent.MouseButton event) {
        if (event.getButton() == 0 && event.getAction() == 1) { // Left click press
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen instanceof UCGScreen ucgScreen) {
                if (ucgScreen.isPotButtonClicked(event.getX(), event.getY())) {
                    handlePotClick();
                }
            }
        }
    }

    private static void handlePotClick() {
        if (ConfigManager.clicksData == null) return;
        
        ClicksData data = ConfigManager.clicksData;
        data.incrementClick();
        
        // Send random phrase
        EasterEggManager.sendPhraseToChat();
        
        // Check for 100th click
        if (data.getClickCount() == 100) {
            triggerScareSequence();
        }
    }

    private static void triggerScareSequence() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;
        
        long currentDayTime = mc.level.getDayTime();
        ConfigManager.clicksData.triggerLock(currentDayTime);
        
        // Hide pot and disable button via screen
        if (mc.screen instanceof UCGScreen ucgScreen) {
            ucgScreen.triggerScareSequence();
        }
        
        // Schedule scare overlay
        int delayTicks = 60 + ThreadLocalRandom.current().nextInt(141); // 60-200 ticks (3-10 seconds)
        mc.execute(() -> {
            // We'll use a tick handler for the delay
            ScareOverlay.trigger(() -> {
                mc.execute(() -> {
                    if (mc.screen instanceof UCGScreen) {
                        mc.setScreen(null);
                    }
                });
            });
        });
    }
}