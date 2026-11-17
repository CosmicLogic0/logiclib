package com.cosmiclogic.logiclib;

//#if MC >= 1.18
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//#else
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//#endif

/**
 * LogicLib: shared code for CosmicLogic's mods. Adds no gameplay on its own.
 *
 * <p>This is the multi-version build (ports/). Modules here: {@code Services.PLATFORM},
 * {@code LogicConfig} and {@code CreatorServer}. {@code LogicRegistry} is only in the 1.21.1 build so far
 * (ask for it in Library Requests if you need it on another version).
 */
public final class LogicLib {

    public static final String MOD_ID = "logiclib";
    public static final String MOD_NAME = "LogicLib";
    //#if MC >= 1.18
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    //#else
    public static final Logger LOG = LogManager.getLogger(MOD_NAME);
    //#endif

    private LogicLib() {
    }

    /** Called once by the loader-specific entrypoint. */
    public static void init() {
        LOG.info("{} loaded on {}", MOD_NAME, com.cosmiclogic.logiclib.platform.Services.PLATFORM.getPlatformName());
    }
}
