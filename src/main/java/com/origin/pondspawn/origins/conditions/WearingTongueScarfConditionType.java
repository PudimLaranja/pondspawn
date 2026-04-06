package com.origin.pondspawn.origins.conditions;

import com.origin.pondspawn.entity.custum.TongueScarf;
import com.origin.pondspawn.origins.init.ModConditionTypes;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.EntityConditionContext;
import io.github.apace100.apoli.condition.type.EntityConditionType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class WearingTongueScarfConditionType extends EntityConditionType {



    @Override
    public boolean test(EntityConditionContext context) {
        Entity entity = context.entity();

        if (entity instanceof PlayerEntity player) {
            for (Entity passenger : player.getPassengerList()) {
                if (passenger instanceof TongueScarf) return true;
            }
        }

        return false;
    }

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return ModConditionTypes.WEARING_TONGUE_SCARF;
    }


}