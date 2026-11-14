package com.cosmiclogic.cosmiclib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CosmicLib: shared code for CosmicLogic's mods. Adds no gameplay on its own.
 *
 * <p>Modules (v0.1.0):
 * <ul>
 *   <li>{@link com.cosmiclogic.cosmiclib.platform.Services} - which loader is running, mod checks, config folder</li>
 *   <li>{@link com.cosmiclogic.cosmiclib.config.CosmicConfig} - simple JSON config files</li>
 *   <li>{@link com.cosmiclogic.cosmiclib.server.CreatorServer} - the "Logic Survival" server-list entry (added once across all mods)</li>
 *   <li>{@link com.cosmiclogic.cosmiclib.registry.CosmicRegistry} - one way to register items/blocks/etc. on Forge and NeoForge</li>
 * </ul>
 */
public final class CosmicLib {

    public static final String MOD_ID = "cosmiclib";
    public static final String MOD_NAME = "CosmicLib";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    private CosmicLib() {
    }

    /** Called once by the loader-specific entrypoint. */
    public static void init() {
        LOG.info("{} loaded on {}", MOD_NAME, com.cosmiclogic.cosmiclib.platform.Services.PLATFORM.getPlatformName());
    }
}
