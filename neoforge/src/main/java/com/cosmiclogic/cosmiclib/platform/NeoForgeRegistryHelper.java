package com.cosmiclogic.cosmiclib.platform;

import com.cosmiclogic.cosmiclib.platform.services.IRegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class NeoForgeRegistryHelper implements IRegistryHelper {

    private static final Map<String, DeferredRegister<?>> REGISTERS = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> Supplier<T> register(ResourceKey<? extends Registry<T>> registry, String modId, String name, Supplier<? extends T> factory) {
        String key = modId + "|" + registry.location();
        DeferredRegister<T> register = (DeferredRegister<T>) REGISTERS.computeIfAbsent(key, k -> create(registry, modId));
        return register.register(name, factory);
    }

    private static <T> DeferredRegister<T> create(ResourceKey<? extends Registry<T>> registry, String modId) {
        DeferredRegister<T> register = DeferredRegister.create(registry, modId);
        // Attaches to the event bus of the mod currently being constructed.
        IEventBus bus = ModLoadingContext.get().getActiveContainer().getEventBus();
        if (bus == null) {
            throw new IllegalStateException("CosmicRegistry.register must be called while mod '" + modId + "' is being constructed");
        }
        register.register(bus);
        return register;
    }
}
