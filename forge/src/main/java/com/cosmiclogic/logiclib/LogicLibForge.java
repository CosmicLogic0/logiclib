package com.cosmiclogic.logiclib;

import com.cosmiclogic.logiclib.client.CreatorServerClient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LogicLib.MOD_ID)
public class LogicLibForge {

    public LogicLibForge() {
        LogicLib.init();
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::onClientSetup);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(CreatorServerClient::process);
    }
}
