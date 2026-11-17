package com.cosmiclogic.logiclib.client;

import com.cosmiclogic.logiclib.LogicLib;
import com.cosmiclogic.logiclib.server.CreatorServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Client-only part of {@link CreatorServer}. Never crashes the game: any problem is logged and skipped.
 *
 * <p>Works the same on every Minecraft version without using any loader events: {@link #start()} waits in the
 * background until the game has started, then does the work on the game's main thread.
 *
 * <p>The two things Minecraft changed between versions (how a {@link ServerData} is made and how it is added to the
 * list) are found by their parameter types, so the same code runs on 1.16.5 through 26.x.
 */
public final class CreatorServerClient {

    private CreatorServerClient() {
    }

    /** Called once from the mod constructor on the client. */
    public static void start() {
        Thread t = new Thread(() -> {
            try {
                // Wait for the game window to exist (up to 10 minutes), plus a few seconds for other mods to request.
                for (int i = 0; i < 600 && Minecraft.getInstance() == null; i++) {
                    Thread.sleep(1000L);
                }
                Thread.sleep(5000L);
                Minecraft mc = Minecraft.getInstance();
                if (mc != null) {
                    mc.execute(CreatorServerClient::process);
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
                add(list, newServerData(CreatorServer.NAME, CreatorServer.ADDRESS));
                list.save();
                LogicLib.LOG.info("Added {} to the server list (requested by {}). It will not be added again.", CreatorServer.NAME, CreatorServer.firstRequester());
            } else {
                LogicLib.LOG.info("{} is already in the server list; it will not be added again.", CreatorServer.NAME);
            }
            CreatorServer.writeMarker(CreatorServer.firstRequester());
        } catch (Throwable t) {
            LogicLib.LOG.warn("Skipped adding the creator server: {}", t.toString());
        }
    }

    /** new ServerData(name, ip, Type.OTHER) on 1.20.2+, new ServerData(name, ip, false) before that. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static ServerData newServerData(String name, String ip) throws Exception {
        for (Constructor<?> c : ServerData.class.getConstructors()) {
            Class<?>[] p = c.getParameterTypes();
            if (p.length != 3 || p[0] != String.class || p[1] != String.class) continue;
            if (p[2] == boolean.class) {
                return (ServerData) c.newInstance(name, ip, false);
            }
            if (p[2].isEnum()) {
                Object other = null;
                Object[] values = p[2].getEnumConstants();
                for (Object v : values) {
                    if (((Enum) v).name().equals("OTHER")) other = v;
                }
                if (other == null) other = values[values.length - 1];
                return (ServerData) c.newInstance(name, ip, other);
            }
        }
        throw new IllegalStateException("no ServerData(String, String, ?) constructor");
    }

    /**
     * list.add(data, false) on newer versions, list.add(data) on older ones. Older versions also have
     * remove(ServerData) with the same shape, so each candidate is checked by whether the list actually grew.
     */
    private static void add(ServerList list, ServerData data) throws Exception {
        java.util.List<Method> two = new java.util.ArrayList<>(), one = new java.util.ArrayList<>();
        for (Method m : ServerList.class.getMethods()) {
            if (Modifier.isStatic(m.getModifiers()) || m.getReturnType() != void.class) continue;
            Class<?>[] p = m.getParameterTypes();
            if (p.length == 2 && p[0] == ServerData.class && p[1] == boolean.class) two.add(m);
            if (p.length == 1 && p[0] == ServerData.class) one.add(m);
        }
        int before = list.size();
        for (Method m : two) {
            m.invoke(list, data, false);
            if (list.size() > before) return;
        }
        for (Method m : one) {
            m.invoke(list, data);
            if (list.size() > before) return;
        }
        throw new IllegalStateException("could not find ServerList.add");
    }
}
