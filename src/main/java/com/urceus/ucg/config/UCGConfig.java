// src/main/java/com/urceus/ucg/config/UCGConfig.java
package com.urceus.ucg.config;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class UCGConfig {
    public static final ModConfigSpec SPEC;
    public static final ClientConfig CLIENT;

    static {
        final Pair<ClientConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT = specPair.getLeft();
        SPEC = specPair.getRight();
    }

    public static class ClientConfig {
        public final ModConfigSpec.ConfigValue<String> defaultTarget;
        public final ModConfigSpec.BooleanValue autoCreative;
        public final ModConfigSpec.BooleanValue enableUrceusWrath;

        ClientConfig(ModConfigSpec.Builder builder) {
            builder.push("general");
            
            defaultTarget = builder
                .comment("Default target selector for commands (@p, @a, @e, etc.)")
                .define("defaultTarget", "@p");
            
            autoCreative = builder
                .comment("Automatically switch to creative mode when executing commands")
                .define("autoCreative", false);
            
            enableUrceusWrath = builder
                .comment("Enable Urceus's Wrath easter egg mechanics")
                .define("enableUrceusWrath", true);
            
            builder.pop();
        }
    }
}