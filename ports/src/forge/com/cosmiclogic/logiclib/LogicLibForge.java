package com.cosmiclogic.logiclib;

import com.cosmiclogic.logiclib.client.CreatorServerClient;
import com.cosmiclogic.logiclib.server.CreatorServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(LogicLib.MOD_ID)
public class LogicLibForge {

    //#if MC >= 26.1
    public LogicLibForge(net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext context) {
    //#else
    public LogicLibForge() {
    //#endif
        LogicLib.init();
        CreatorServer.requestForSelfTestIfAsked();
        if (FMLEnvironment.dist == Dist.CLIENT) {
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
