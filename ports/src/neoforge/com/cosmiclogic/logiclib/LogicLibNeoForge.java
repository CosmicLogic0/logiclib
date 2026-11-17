package com.cosmiclogic.logiclib;

import com.cosmiclogic.logiclib.client.CreatorServerClient;
import com.cosmiclogic.logiclib.platform.NeoForgePlatformHelper;
import com.cosmiclogic.logiclib.server.CreatorServer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(LogicLib.MOD_ID)
public class LogicLibNeoForge {

    public LogicLibNeoForge(IEventBus modBus, Dist dist) {
        NeoForgePlatformHelper.dist = dist;
        LogicLib.init();
        CreatorServer.requestForSelfTestIfAsked();
        if (dist.isClient()) {
            ClientStart.run();
        }
    }

    /** In its own class so dedicated servers never load client code. */
    static final class ClientStart {
        static void run() {
            CreatorServerClient.start();
        }
    }
}
