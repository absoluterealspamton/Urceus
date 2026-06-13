// src/main/java/com/urceus/ucg/UCGMod.java
package com.urceus.ucg;

import com.mojang.logging.LogUtils;
import com.urceus.ucg.config.UCGConfig;
import com.urceus.ucg.events.ClickTracker;
import com.urceus.ucg.events.KeyInputHandler;
import com.urceus.ucg.events.QoLEventHandler;
import com.urceus.ucg.registry.UCGKeyMappings;
import com.urceus.ucg.util.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(UCGMod.MODID)
public class UCGMod {
    public static final String MODID = "ucg";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String CONFIG_DIR = "config/ucg";

    public UCGMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register config
        modContainer.registerConfig(ModConfig.Type.CLIENT, UCGConfig.SPEC);
        
        // Register event buses
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(new ClickTracker());
        NeoForge.EVENT_BUS.register(new QoLEventHandler());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ConfigManager.init();
        EasterEggManager.init();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        KeyInputHandler.init();
        UCGKeyMappings.register();
    }

    @SubscribeEvent
    public void onKeyMappingsRegister(RegisterKeyMappingsEvent event) {
        UCGKeyMappings.registerKeyMappings(event);
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}