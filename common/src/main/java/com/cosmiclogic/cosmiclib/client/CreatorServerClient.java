package com.cosmiclogic.cosmiclib.client;

import com.cosmiclogic.cosmiclib.CosmicLib;
import com.cosmiclogic.cosmiclib.server.CreatorServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;

/**
 * Client-only part of {@link CreatorServer}. Only ever called on the game client after startup.
 * Never crashes the game: any problem is logged and skipped.
 */
public final class CreatorServerClient {

    private CreatorServerClient() {
    }

    public static void process() {
        try {
            if (!CreatorServer.hasRequests() || CreatorServer.markerExists()) {
                return;
            }
            Minecraft minecraft = Minecraft.getInstance();
            ServerList list = new ServerList(minecraft);
            list.load();

            boolean alreadyThere = false;
            for (int i = 0; i < list.size(); i++) {
                ServerData data = list.get(i);
                if (data != null && data.ip != null && data.ip.trim().equalsIgnoreCase(CreatorServer.ADDRESS)) {
                    alreadyThere = true;
                    break;
                }
            }

            if (!alreadyThere) {
                // Appended at the END of the list, never pinned or locked.
                list.add(new ServerData(CreatorServer.NAME, CreatorServer.ADDRESS, ServerData.Type.OTHER), false);
                list.save();
                CosmicLib.LOG.info("Added {} to the server list (requested by {}). It will not be added again.", CreatorServer.NAME, CreatorServer.firstRequester());
            }
            CreatorServer.writeMarker(CreatorServer.firstRequester());
        } catch (Throwable t) {
            CosmicLib.LOG.warn("Skipped adding the creator server: {}", t.toString());
        }
    }
}
