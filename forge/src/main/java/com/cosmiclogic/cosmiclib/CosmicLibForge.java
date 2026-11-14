package com.cosmiclogic.cosmiclib;

import com.cosmiclogic.cosmiclib.client.CreatorServerClient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CosmicLib.MOD_ID)
public class CosmicLibForge {

    public CosmicLibForge() {
        CosmicLib.init();
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::onClientSetup);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(CreatorServerClient::process);
    }
}
