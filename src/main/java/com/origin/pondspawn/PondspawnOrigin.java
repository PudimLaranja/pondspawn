package com.origin.pondspawn;

import com.origin.pondspawn.init.*;
import com.origin.pondspawn.origins.init.ModActionTypes;
import com.origin.pondspawn.origins.init.ModConditionTypes;
import com.origin.pondspawn.origins.init.ModPowerTypes;
import com.origin.pondspawn.util.TickScheduler;
import com.origin.pondspawn.weightSystem.WeightManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


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
        ModEntityTypes.load();
        ModConditionTypes.register();
        ModActionTypes.register();
        ModPowerTypes.register();
        ModParticles.register();

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            for (ServerWorld world : server.getWorlds()) {
                world.iterateEntities().forEach(entity -> {
                    if (
                            List.of(
                                    ModEntityTypes.TONGUE_ENTITY_TYPE,
                                    ModEntityTypes.TONGUE_TIP_ENTITY_TYPE,
                                    ModEntityTypes.TONGUE_SCARF_ENTITY_TYPE,
                                    ModEntityTypes.TONGUE_SCARF_MOUTH_ENTITY_TYPE
                            ).contains(entity.getType())) {
                        entity.kill();
                    }
                });
            }
        });

        ServerPlayConnectionEvents.DISCONNECT.register(((handler, server) -> {
            PlayerEntity player = handler.getPlayer();
            PlayerWithTongueData tongueData = (PlayerWithTongueData) player;

            tongueData.pondspawn$getTongueEntity().kill();
            tongueData.pondspawn$getScarfEntity().kill();
        }));
        LOGGER.info("Loaded:]");
	}

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID,path);
    }
}