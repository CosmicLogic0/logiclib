package com.cosmiclogic.cosmiclib.platform.services;

import java.nio.file.Path;

public interface IPlatformHelper {

    /** "Forge" or "NeoForge". */
    String getPlatformName();

    /** True if a mod with this id is installed. */
    boolean isModLoaded(String modId);

    /** True when running from a development environment (IDE), false in a real game. */
    boolean isDevelopmentEnvironment();

    /** The game's config folder (usually .minecraft/config). */
    Path getConfigDir();

    /** True on the game client (physical client), false on a dedicated server. */
    boolean isClient();

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }
}
