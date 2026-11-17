package com.cosmiclogic.logiclib.platform;

import com.cosmiclogic.logiclib.platform.services.IPlatformHelper;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

import java.io.File;
import java.nio.file.Path;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return Loader.isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        Object dev = Launch.blackboard == null ? null : Launch.blackboard.get("fml.deobfuscatedEnvironment");
        return Boolean.TRUE.equals(dev);
    }

    @Override
    public Path getConfigDir() {
        File dir = Loader.instance().getConfigDir();
        return (dir != null ? dir : new File("config")).toPath();
    }

    @Override
    public boolean isClient() {
        return FMLLaunchHandler.side().isClient();
    }
}
