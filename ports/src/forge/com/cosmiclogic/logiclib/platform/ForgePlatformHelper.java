package com.cosmiclogic.logiclib.platform;

import com.cosmiclogic.logiclib.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        //#if MC >= 26.1
        // Forge 26 changed how ModList is reached, so look it up by name instead of guessing one exact method.
        try {
            for (java.lang.reflect.Method m : ModList.class.getMethods()) {
                if (java.lang.reflect.Modifier.isStatic(m.getModifiers()) && m.getName().equals("isLoaded")
                        && m.getParameterCount() == 1 && m.getParameterTypes()[0] == String.class) {
                    return (Boolean) m.invoke(null, modId);
                }
            }
            for (java.lang.reflect.Method m : ModList.class.getMethods()) {
                if (java.lang.reflect.Modifier.isStatic(m.getModifiers()) && m.getParameterCount() == 0
                        && ModList.class.isAssignableFrom(m.getReturnType())) {
                    Object list = m.invoke(null);
                    return (Boolean) ModList.class.getMethod("isLoaded", String.class).invoke(list, modId);
                }
            }
        } catch (Throwable ignored) {
        }
        return false;
        //#else
        return ModList.get().isLoaded(modId);
        //#endif
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
        return FMLEnvironment.dist.isClient();
    }
}
