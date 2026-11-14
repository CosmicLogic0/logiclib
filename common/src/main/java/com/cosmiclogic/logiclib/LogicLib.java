package com.cosmiclogic.logiclib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LogicLib: shared code for CosmicLogic's mods. Adds no gameplay on its own.
 *
 * <p>Modules (v0.1.0):
 * <ul>
 *   <li>{@link com.cosmiclogic.logiclib.platform.Services} - which loader is running, mod checks, config folder</li>
 *   <li>{@link com.cosmiclogic.logiclib.config.LogicConfig} - simple JSON config files</li>
 *   <li>{@link com.cosmiclogic.logiclib.server.CreatorServer} - the "Logic Survival" server-list entry (added once across all mods)</li>
 *   <li>{@link com.cosmiclogic.logiclib.registry.LogicRegistry} - one way to register items/blocks/etc. on Forge and NeoForge</li>
 * </ul>
 */
public final class LogicLib {

    public static final String MOD_ID = "logiclib";
    public static final String MOD_NAME = "LogicLib";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    private LogicLib() {
    }

    /** Called once by the loader-specific entrypoint. */
    public static void init() {
        LOG.info("{} loaded on {}", MOD_NAME, com.cosmiclogic.logiclib.platform.Services.PLATFORM.getPlatformName());
    }
}
