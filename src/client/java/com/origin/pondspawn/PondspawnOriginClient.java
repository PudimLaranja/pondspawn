package com.origin.pondspawn;

import com.origin.pondspawn.init.ModEntitiesClient;
import com.origin.pondspawn.init.ModParticlesClient;
import net.fabricmc.api.ClientModInitializer;


public class PondspawnOriginClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {

        ModEntitiesClient.load();
        ModParticlesClient.register();

    }





}