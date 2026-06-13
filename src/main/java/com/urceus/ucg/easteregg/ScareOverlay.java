// src/main/java/com/urceus/ucg/easteregg/ScareOverlay.java
package com.urceus.ucg.easteregg;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.urceus.ucg.UCGMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class ScareOverlay {
    private static final ResourceLocation SCARE_TEXTURE = ResourceLocation.fromNamespaceAndPath(
        "minecraft", "textures/block/decorated_pot_side.png");
    
    private static boolean active = false;
    private static long startTime = 0;
    private static int duration = 0;
    private static Runnable onEndCallback;

    public static void trigger(Runnable onEnd) {
        active = true;
        startTime = System.currentTimeMillis();
        duration = 3000 + ThreadLocalRandom.current().nextInt(7000); // 3-10 seconds
        onEndCallback = onEnd;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.player != null) {
            mc.player.playSound(SoundEvents.AMBIENT_CAVE, SoundSource.MASTER, 1.0f, 1.0f);
        }
    }

    public static void render(GuiGraphics guiGraphics, int width, int height) {
        if (!active) return;
        
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed >= duration) {
            active = false;
            if (onEndCallback != null) {
                onEndCallback.run();
            }
            return;
        }

        // Render full-screen scare texture
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        
        float alpha = Math.min(1.0f, (float) elapsed / 500.0f); // Fade in over 500ms
        if (elapsed > duration - 500) {
            alpha = Math.max(0.0f, 1.0f - (float)(elapsed - (duration - 500)) / 500.0f); // Fade out
        }
        
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, alpha);
        guiGraphics.blit(SCARE_TEXTURE, 0, 0, 0, 0, width, height, width, height);
        guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        
        RenderSystem.disableBlend();
    }

    public static boolean isActive() {
        return active;
    }
}