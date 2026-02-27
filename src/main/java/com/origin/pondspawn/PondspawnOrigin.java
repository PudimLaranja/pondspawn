package com.origin.pondspawn;

import com.origin.pondspawn.init.*;
import com.origin.pondspawn.origins.init.ModActionTypes;
import com.origin.pondspawn.origins.init.ModConditionTypes;
import com.origin.pondspawn.util.TickScheduler;
import com.origin.pondspawn.weightSystem.WeightManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PondspawnOrigin implements ModInitializer {
	public static final String MOD_ID = "pondspawn";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution
        LOGGER.info("Loading...");

        ResourceManagerHelper.get(ResourceType.SERVER_DATA)
                .registerReloadListener(new WeightManager());
        ServerTickEvents.END_SERVER_TICK.register(TickScheduler::onServerTick);

        ModItems.load();
        ModCommands.load();
        ModEntities.load();
        ModConditionTypes.register();
        ModActionTypes.register();
        LOGGER.info("Loaded:]");

	}

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID,path);
    }
}