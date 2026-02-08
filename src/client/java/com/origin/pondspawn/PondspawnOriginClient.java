package com.origin.pondspawn;

import com.origin.pondspawn.entity.ModEntitiesClient;
import net.fabricmc.api.ClientModInitializer;


public class PondspawnOriginClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {

        ModEntitiesClient.load();

    }





}