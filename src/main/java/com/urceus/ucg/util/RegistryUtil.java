// src/main/java/com/urceus/ucg/util/RegistryUtil.java
package com.urceus.ucg.util;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public class RegistryUtil {
    public static Optional<Item> getItem(ResourceLocation id) {
        return BuiltInRegistries.ITEM.getOptional(id);
    }

    public static Optional<Item> getItem(String id) {
        return getItem(ResourceLocation.parse(id));
    }

    public static Optional<EntityType<?>> getEntityType(ResourceLocation id) {
        return BuiltInRegistries.ENTITY_TYPE.getOptional(id);
    }

    public static Optional<EntityType<?>> getEntityType(String id) {
        return getEntityType(ResourceLocation.parse(id));
    }

    public static Optional<Block> getBlock(ResourceLocation id) {
        return BuiltInRegistries.BLOCK.getOptional(id);
    }

    public static Optional<Block> getBlock(String id) {
        return getBlock(ResourceLocation.parse(id));
    }

    public static <T> Optional<T> getFromRegistry(Registry<T> registry, ResourceLocation id) {
        return registry.getOptional(id);
    }
}