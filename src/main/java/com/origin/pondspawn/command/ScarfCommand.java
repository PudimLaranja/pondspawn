package com.origin.pondspawn.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.datafixers.TypeRewriteRule;
import com.origin.pondspawn.PlayerWithTongueData;
import com.origin.pondspawn.entity.custum.TongueScarf;
import com.origin.pondspawn.entity.custum.TongueScarfMouth;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.World;

import java.util.Objects;

public class ScarfCommand {

    public static int toggle(CommandContext<ServerCommandSource> context) {
        Entity entity = context.getSource().getEntity();

        if (!(entity instanceof PlayerEntity player)) return -1;

        TongueScarf scarf;
        PlayerWithTongueData tongueData = (PlayerWithTongueData) player;

        World world = player.getWorld();
        if (tongueData.pondspawn$getScarfEntity() instanceof TongueScarf playerScarf) {
            tongueData.pondspawn$setScarfEntity(null);
            if (playerScarf.getFirstPassenger() instanceof TongueScarfMouth mouth) mouth.kill();
            playerScarf.kill();
            return 1;
        } else {
            scarf = TongueScarf.factory(world,player);
        }

        ((PlayerWithTongueData) player).pondspawn$setScarfEntity(scarf);

        scarf.startRiding(player,true);

        TongueScarfMouth mouth = TongueScarfMouth.factory(world,scarf);

        mouth.startRiding(scarf);

        world.spawnEntity(scarf);
        world.spawnEntity(mouth);
        return 0;
    }

    public static int spawn(CommandContext<ServerCommandSource> context) {

        Entity entity = context.getSource().getEntity();

        if (!(entity instanceof PlayerEntity player)) return -1;

        TongueScarf scarf;
        PlayerWithTongueData tongueData = (PlayerWithTongueData) player;

        World world = player.getWorld();
        if (tongueData.pondspawn$getScarfEntity() instanceof TongueScarf) {
            return 1;
        } else {
            scarf = TongueScarf.factory(world,player);
        }

        ((PlayerWithTongueData) player).pondspawn$setScarfEntity(scarf);

        scarf.startRiding(player,true);

        TongueScarfMouth mouth = TongueScarfMouth.factory(world,scarf);

        mouth.startRiding(scarf);

        world.spawnEntity(scarf);
        world.spawnEntity(mouth);
        return 0;
    }

    public static int kill(CommandContext<ServerCommandSource> context) {
        Entity entity = context.getSource().getEntity();

        if (!(entity instanceof PlayerEntity player)) return -1;

        PlayerWithTongueData tongueData = (PlayerWithTongueData) player;

        if (tongueData.pondspawn$getScarfEntity() instanceof TongueScarf playerScarf) {
            tongueData.pondspawn$setScarfEntity(null);
            if (playerScarf.getFirstPassenger() instanceof TongueScarfMouth mouth) mouth.kill();
            playerScarf.kill();
            return 1;
        }

        return 0;
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((
                dispatcher,
                registryAcess,
                envioronment
                ) -> {
            dispatcher.register(CommandManager.literal("scarf")
                    .then(CommandManager.literal("toggle").executes(ScarfCommand::toggle))
                    .then(CommandManager.literal("spawn").executes(ScarfCommand::spawn))
                    .then(CommandManager.literal("kill").executes(ScarfCommand::kill))
            );
        });
    }
}
