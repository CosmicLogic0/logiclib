package com.cosmiclogic.cosmiclib.platform.services;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public interface IRegistryHelper {

    /**
     * Registers something (item, block, sound, ...) for a mod. Must be called while that mod is
     * being constructed (in its mod class constructor, or in static fields loaded from there).
     */
    <T> Supplier<T> register(ResourceKey<? extends Registry<T>> registry, String modId, String name, Supplier<? extends T> factory);
}
