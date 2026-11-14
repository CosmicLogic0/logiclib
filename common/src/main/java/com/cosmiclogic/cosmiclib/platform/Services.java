package com.cosmiclogic.cosmiclib.platform;

import com.cosmiclogic.cosmiclib.CosmicLib;
import com.cosmiclogic.cosmiclib.platform.services.IPlatformHelper;
import com.cosmiclogic.cosmiclib.platform.services.IRegistryHelper;

import java.util.ServiceLoader;

/**
 * Loader-specific helpers, found at runtime through Java's ServiceLoader.
 * The Forge and NeoForge modules each provide their own implementation
 * (see META-INF/services in each module).
 */
public final class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IRegistryHelper REGISTRY = load(IRegistryHelper.class);

    private Services() {
    }

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        CosmicLib.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
