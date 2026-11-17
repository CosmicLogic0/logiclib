package com.cosmiclogic.logiclib;

import com.cosmiclogic.logiclib.platform.Services;
import com.cosmiclogic.logiclib.server.CreatorServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * LogicLib for Forge 1.12.2: shared code for CosmicLogic's mods. Adds no gameplay on its own.
 *
 * <p>Same API as the other versions: {@code Services.PLATFORM}, {@code LogicConfig} and {@code CreatorServer}.
 * Mods that use it add {@code dependencies = "required-after:logiclib@[0.1.0,)"} to their {@code @Mod}.
 * Players without LogicLib can still join a server that has it ({@code acceptableRemoteVersions = "*"}).
 */
@Mod(modid = LogicLib.MOD_ID, name = LogicLib.MOD_NAME, version = LogicLib.VERSION,
        acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.12.2]")
public final class LogicLib {

    public static final String MOD_ID = "logiclib";
    public static final String MOD_NAME = "LogicLib";
    public static final String VERSION = "0.1.0";
    public static final Logger LOG = LogManager.getLogger(MOD_NAME);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        init();
        CreatorServer.requestForSelfTestIfAsked();
        if (event.getSide().isClient()) {
            ClientStart.run();
        }
    }

    /** Logs that LogicLib is running (same line on every version). */
    public static void init() {
        LOG.info("{} loaded on {}", MOD_NAME, Services.PLATFORM.getPlatformName());
    }

    /** In its own class so dedicated servers never load client code. */
    static final class ClientStart {
        static void run() {
            com.cosmiclogic.logiclib.client.CreatorServerClient.start();
        }
    }
}
