package com.cosmiclogic.logiclib.registry;

import com.cosmiclogic.logiclib.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

/**
 * One way to register things that works on both Forge and NeoForge.
 *
 * <pre>{@code
 * public static final Supplier<Item> RUBY =
 *     LogicRegistry.register(Registries.ITEM, MOD_ID, "ruby", () -> new Item(new Item.Properties()));
 * }</pre>
 *
 * Call it while your mod is being constructed (your mod class constructor, or static fields of a
 * class you touch from there). Use {@code RUBY.get()} only after registration has finished.
 */
public final class LogicRegistry {

    private LogicRegistry() {
    }

    public static <T> Supplier<T> register(ResourceKey<? extends Registry<T>> registry, String modId, String name, Supplier<? extends T> factory) {
        return Services.REGISTRY.register(registry, modId, name, factory);
    }
}
