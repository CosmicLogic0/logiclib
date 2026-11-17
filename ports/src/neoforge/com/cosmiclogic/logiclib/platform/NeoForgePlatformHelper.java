package com.cosmiclogic.logiclib.platform;

import com.cosmiclogic.logiclib.platform.services.IPlatformHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class NeoForgePlatformHelper implements IPlatformHelper {

    /** Set by the mod constructor. */
    public static Dist dist;

    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        try {
            return !(Boolean) FMLLoader.class.getMethod("isProduction").invoke(null);
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public boolean isClient() {
        return dist != null && dist.isClient();
    }
}
