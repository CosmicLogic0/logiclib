package com.cosmiclogic.cosmiclib.server;

import com.cosmiclogic.cosmiclib.CosmicLib;
import com.cosmiclogic.cosmiclib.platform.Services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Adds the creator's server, Logic Survival, to the end of the player's Multiplayer list ONCE
 * across ALL CosmicLogic mods (see "Server Promotion Policy" in the Mod Main Hub).
 *
 * <p>Every mod that wants it calls, during construction:
 * <pre>{@code
 * CreatorServer.request(MOD_ID, CONFIG.get().addCreatorServer);
 * }</pre>
 * CosmicLib then adds the entry on the client after startup, unless the shared marker file
 * {@code config/cosmiclogic/creator_server.json} already exists. The marker is written even if the
 * server was already in the list, so it is never added again, even after the player deletes it.
 *
 * <p>Mods that do NOT use CosmicLib must follow the same rules and use the same marker path.
 */
public final class CreatorServer {

    public static final String NAME = "Logic Survival";
    public static final String ADDRESS = "play.logicsurvival.fyi";

    private static final Set<String> REQUESTS = Collections.synchronizedSet(new LinkedHashSet<>());

    private CreatorServer() {
    }

    /**
     * Asks for the server entry to be added. Pass the mod's own config value so players and modpack
     * makers can turn it off ({@code addCreatorServer = false}).
     */
    public static void request(String modId, boolean enabled) {
        if (enabled) {
            REQUESTS.add(modId);
        }
    }

    public static boolean hasRequests() {
        return !REQUESTS.isEmpty();
    }

    public static String firstRequester() {
        synchronized (REQUESTS) {
            return REQUESTS.isEmpty() ? "unknown" : REQUESTS.iterator().next();
        }
    }

    /** The shared marker that every CosmicLogic mod checks: config/cosmiclogic/creator_server.json */
    public static Path markerPath() {
        return Services.PLATFORM.getConfigDir().resolve("cosmiclogic").resolve("creator_server.json");
    }

    public static boolean markerExists() {
        return Files.exists(markerPath());
    }

    public static void writeMarker(String byModId) {
        Path marker = markerPath();
        String json = "{\n  \"added\": true,\n  \"by\": \"" + byModId.replace("\"", "") + "\",\n  \"date\": \"" + Instant.now() + "\"\n}\n";
        try {
            Files.createDirectories(marker.getParent());
            Files.writeString(marker, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            CosmicLib.LOG.warn("Could not write creator server marker: {}", e.getMessage());
        }
    }
}
