package com.origin.pondspawn.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushCommand {

    private static final Logger log = LoggerFactory.getLogger(PushCommand.class);

    public static int commandLogic(CommandContext<ServerCommandSource> context) {


        double dx = DoubleArgumentType.getDouble(context,"dirX");
        double dy = DoubleArgumentType.getDouble(context,"dirY");
        double dz = DoubleArgumentType.getDouble(context,"dirZ");

        double force = DoubleArgumentType.getDouble(context,"force");

        Entity entity = context.getSource().getEntity();

        if (entity == null) return -1;

        Vec3d forward = entity.getRotationVector().multiply(1, 0, 1).normalize();

        Vec3d right = new Vec3d(-forward.z, 0, forward.x);

        Vec3d relativeMove = forward.multiply(dz)
                .add(right.multiply(dx))
                .add(0, dy, 0);

        if (relativeMove == Vec3d.ZERO) return 0;

        relativeMove = relativeMove.normalize().multiply(force);

        entity.addVelocity(relativeMove);

        if (entity instanceof PlayerEntity player) {
            player.velocityModified = true;
        }

        return 0;
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((
                dispatcher,
                registryAcess,
                envioronment
        ) -> {
            dispatcher.register(CommandManager.literal("push")
                    .then(
                            CommandManager.argument("force", DoubleArgumentType.doubleArg()).then(
                            CommandManager.argument("dirX",DoubleArgumentType.doubleArg(-1,1)).then(
                            CommandManager.argument("dirY",DoubleArgumentType.doubleArg(-1,1)).then(
                            CommandManager.argument("dirZ", DoubleArgumentType.doubleArg(-1,1))
                                    .executes(PushCommand::commandLogic)
                    ))))
            );
        });
    }
}
