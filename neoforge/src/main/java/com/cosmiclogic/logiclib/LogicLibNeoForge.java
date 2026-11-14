package com.cosmiclogic.logiclib;

import com.cosmiclogic.logiclib.client.CreatorServerClient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(LogicLib.MOD_ID)
public class LogicLibNeoForge {

    public LogicLibNeoForge(IEventBus modBus) {
        LogicLib.init();
        modBus.addListener(this::onClientSetup);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(CreatorServerClient::process);
    }
}
