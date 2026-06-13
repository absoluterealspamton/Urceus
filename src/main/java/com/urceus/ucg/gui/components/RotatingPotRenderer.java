// src/main/java/com/urceus/ucg/gui/components/RotatingPotRenderer.java
package com.urceus.ucg.gui.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RotatingPotRenderer {
    private final BlockState state;
    private final DecoratedPotBlockEntity dummyBlockEntity;

    public RotatingPotRenderer(BlockState state) {
        this.state = state;
        this.dummyBlockEntity = new DecoratedPotBlockEntity(BlockPos.ZERO, state);
        // Set some patterns for visual variety
        this.dummyBlockEntity.setSherds(
            net.minecraft.world.item.Items.SHERD_ARCHER,
            net.minecraft.world.item.Items.SHERD_PRIZE,
            net.minecraft.world.item.Items.SHERD_ARMS_UP,
            net.minecraft.world.item.Items.SHERD_SKULL
        );
    }

    public void render(GuiGraphics guiGraphics) {
        Minecraft mc = Minecraft.getInstance();
        BlockRenderDispatcher blockRenderer = mc.getBlockRenderer();
        BlockEntityRenderDispatcher blockEntityRenderer = mc.getBlockEntityRenderDispatcher();
        
        PoseStack pose = guiGraphics.pose();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        
        // Render block
        blockRenderer.renderSingleBlock(state, pose, bufferSource, 0xF000F0, OverlayTexture.NO_OVERLAY);
        
        // Render block entity (for sherds)
        blockEntityRenderer.render(dummyBlockEntity, 0.0f, pose, bufferSource, 0xF000F0, OverlayTexture.NO_OVERLAY);
        
        bufferSource.endBatch(RenderType.solid());
    }
}