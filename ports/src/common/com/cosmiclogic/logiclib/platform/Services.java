package com.cosmiclogic.logiclib.platform;

import com.cosmiclogic.logiclib.LogicLib;
import com.cosmiclogic.logiclib.platform.services.IPlatformHelper;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Loader-specific helpers, found at runtime through Java's ServiceLoader
 * (META-INF/services in each loader's resources).
 */
public final class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    private Services() {
    }

    public static <T> T load(Class<T> clazz) {
        Iterator<T> it = ServiceLoader.load(clazz, Services.class.getClassLoader()).iterator();
        if (!it.hasNext()) {
            throw new NullPointerException("Failed to load service for " + clazz.getName());
        }
        T loaded = it.next();
        LogicLib.LOG.debug("Loaded {} for service {}", loaded, clazz);
        return loaded;
    }
}
