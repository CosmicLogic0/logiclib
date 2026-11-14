package com.cosmiclogic.cosmiclib;

import com.cosmiclogic.cosmiclib.client.CreatorServerClient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(CosmicLib.MOD_ID)
public class CosmicLibNeoForge {

    public CosmicLibNeoForge(IEventBus modBus) {
        CosmicLib.init();
        modBus.addListener(this::onClientSetup);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(CreatorServerClient::process);
    }
}
