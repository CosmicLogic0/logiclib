package com.cosmiclogic.logiclib.platform;

import com.cosmiclogic.logiclib.platform.services.IRegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ForgeRegistryHelper implements IRegistryHelper {

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
        register.register(FMLJavaModLoadingContext.get().getModEventBus());
        return register;
    }
}
