// src/main/java/com/urceus/ucg/util/CommandUtil.java
package com.urceus.ucg.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.stream.Collectors;

public class CommandUtil {
    public static String createGiveCommand(ItemStack stack) {
        if (stack.isEmpty()) return "";
        
        StringBuilder cmd = new StringBuilder("/give @p ");
        cmd.append(stack.getItem().toString()).append(" ").append(stack.getCount());
        
        // Add NBT if present
        CompoundTag tag = stack.save(new CompoundTag());
        // Remove count and id as they're in the command
        tag.remove("count");
        tag.remove("id");
        
        if (!tag.isEmpty()) {
            cmd.append(" ").append(tag.toString());
        }
        
        return cmd.toString();
    }

    public static String createSummonCommand(LivingEntity entity) {
        StringBuilder cmd = new StringBuilder("/summon ");
        cmd.append(entity.getType().toString()).append(" ~ ~ ~ ");
        
        CompoundTag tag = entity.saveWithoutId(new CompoundTag());
        // Remove UUID and Pos as we're spawning at ~ ~ ~
        tag.remove("UUID");
        tag.remove("Pos");
        
        if (!tag.isEmpty()) {
            cmd.append(tag.toString());
        }
        
        return cmd.toString();
    }
}