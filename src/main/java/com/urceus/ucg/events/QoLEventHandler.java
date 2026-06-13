// src/main/java/com/urceus/ucg/events/QoLEventHandler.java
package com.urceus.ucg.events;

import com.urceus.ucg.UCGMod;
import com.urceus.ucg.util.CommandUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = UCGMod.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class QoLEventHandler {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (event.getAction() != 1) return; // Only on press
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen != null) return; // Don't trigger when GUI open
        
        // Ctrl + Middle Click (button 2)
        if (event.getKey() == 2 && mc.options.keyShift.isDown()) {
            // Item copy - check hovered slot
            if (mc.player != null && mc.player.containerMenu != null) {
                int slotIndex = mc.player.containerMenu.getSlotUnderMouse();
                if (slotIndex >= 0) {
                    ItemStack stack = mc.player.containerMenu.getSlot(slotIndex).getItem();
                    if (!stack.isEmpty()) {
                        String command = CommandUtil.createGiveCommand(stack);
                        copyToClipboard(command);
                        mc.player.sendSystemMessage(
                            net.minecraft.network.chat.Component.literal("Copied: " + command)
                                .withStyle(net.minecraft.ChatFormatting.GREEN)
                        );
                        return;
                    }
                }
            }
            
            // Entity copy - check crosshair target
            if (mc.crosshairPickEntity instanceof LivingEntity entity) {
                String command = CommandUtil.createSummonCommand(entity);
                copyToClipboard(command);
                mc.player.sendSystemMessage(
                    net.minecraft.network.chat.Component.literal("Copied: " + command)
                        .withStyle(net.minecraft.ChatFormatting.GREEN)
                );
            }
        }
    }

    private static void copyToClipboard(String text) {
        Minecraft.getInstance().keyboardHandler.setClipboard(text);
    }
}