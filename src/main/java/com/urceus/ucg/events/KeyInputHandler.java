// src/main/java/com/urceus/ucg/events/KeyInputHandler.java
package com.urceus.ucg.events;

import com.urceus.ucg.UCGMod;
import com.urceus.ucg.gui.UCGScreen;
import com.urceus.ucg.registry.UCGKeyMappings;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = UCGMod.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class KeyInputHandler {
    public static void init() {
        // Registration happens via @SubscribeEvent
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (UCGKeyMappings.OPEN_UCG.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen == null && mc.player != null) {
                // Check if locked
                if (ConfigManager.clicksData != null && 
                    ConfigManager.clicksData.isLocked() && 
                    mc.level != null) {
                    long dayTime = mc.level.getDayTime();
                    if (!ConfigManager.clicksData.checkLockExpired(dayTime)) {
                        mc.player.sendSystemMessage(
                            net.minecraft.network.chat.Component.literal("Urceus non amat molestos homines.")
                                .withStyle(net.minecraft.ChatFormatting.RED)
                        );
                        return;
                    }
                }
                mc.setScreen(new UCGScreen());
            }
        }
    }
}