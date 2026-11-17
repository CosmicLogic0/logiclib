package com.cosmiclogic.logiclib.client;

import com.cosmiclogic.logiclib.LogicLib;
import com.cosmiclogic.logiclib.server.CreatorServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;

/**
 * Client-only part of {@link CreatorServer} for 1.12.2. Never crashes the game: any problem is logged and skipped.
 * Same behavior as the other versions: waits until the game has started, then (on the game's main thread) adds
 * Logic Survival to the END of the server list once, unless the shared marker already exists.
 */
public final class CreatorServerClient {

    private CreatorServerClient() {
    }

    /** Called once from LogicLib's preInit on the client. */
    public static void start() {
        Thread t = new Thread(() -> {
            try {
                for (int i = 0; i < 600 && Minecraft.getMinecraft() == null; i++) {
                    Thread.sleep(1000L);
                }
                // A few seconds for other mods to make their request. The task itself runs on the main thread
                // once the game loop is running (after loading has finished).
                Thread.sleep(5000L);
                Minecraft mc = Minecraft.getMinecraft();
                if (mc != null) {
                    mc.addScheduledTask(CreatorServerClient::process);
                }
            } catch (Throwable e) {
                LogicLib.LOG.warn("Skipped adding the creator server: {}", e.toString());
            }
        }, "LogicLib creator server");
        t.setDaemon(true);
        t.start();
    }

    public static void process() {
        try {
            if (!CreatorServer.hasRequests() || CreatorServer.markerExists()) {
                return;
            }
            ServerList list = new ServerList(Minecraft.getMinecraft());
            list.loadServerList();

            boolean alreadyThere = false;
            for (int i = 0; i < list.countServers(); i++) {
                ServerData data = list.getServerData(i);
                if (data != null && data.serverIP != null && data.serverIP.trim().equalsIgnoreCase(CreatorServer.ADDRESS)) {
                    alreadyThere = true;
                    break;
                }
            }

            if (!alreadyThere) {
                // Appended at the END of the list, never pinned or locked.
                list.addServerData(new ServerData(CreatorServer.NAME, CreatorServer.ADDRESS, false));
                list.saveServerList();
                LogicLib.LOG.info("Added {} to the server list (requested by {}). It will not be added again.", CreatorServer.NAME, CreatorServer.firstRequester());
            } else {
                LogicLib.LOG.info("{} is already in the server list; it will not be added again.", CreatorServer.NAME);
            }
            CreatorServer.writeMarker(CreatorServer.firstRequester());
        } catch (Throwable t) {
            LogicLib.LOG.warn("Skipped adding the creator server: {}", t.toString());
        }
    }
}
