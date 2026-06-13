// src/main/java/com/urceus/ucg/registry/UCGKeyMappings.java
package com.urceus.ucg.registry;

import com.urceus.ucg.UCGMod;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = UCGMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class UCGKeyMappings {
    public static final KeyMapping OPEN_UCG = new KeyMapping(
        "key.ucg.open",
        KeyConflictContext.IN_GAME,
        GLFW.GLFW_KEY_U,
        "key.categories.ucg"
    );

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_UCG);
    }
}