package com.origin.pondspawn.origins.conditions;

import com.origin.pondspawn.PondspawnOrigin;
import dev.architectury.event.events.common.TickEvent;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerReference;
import io.github.apace100.apoli.power.type.CooldownPowerType;
import io.github.apace100.apoli.power.type.VariableIntPowerType;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;

public class ArmorEquippedCondition {
    public static boolean condition(Entity entity, String piece) {

        if (entity instanceof PlayerEntity player) {
            switch (piece) {
                case "helmet" -> {
                    return !player.getEquippedStack(EquipmentSlot.HEAD).isEmpty();
                }
                case "chestplate" -> {
                    return !player.getEquippedStack(EquipmentSlot.CHEST).isEmpty();
                }
                case "leggings" -> {
                    return !player.getEquippedStack(EquipmentSlot.LEGS).isEmpty();
                }
                case "boots" -> {
                    return !player.getEquippedStack(EquipmentSlot.FEET).isEmpty();
                }
                case null, default -> {
                    return false;
                }
            }

        }

        return false;
    }

    public static ConditionTypeFactory<Entity> getFactory() {
        return new ConditionTypeFactory<>(
                PondspawnOrigin.id("armor_equipped"),
                new SerializableData()
                        .add("piece",SerializableDataTypes.STRING ),
                (data, entity) -> condition(entity,
                        data.get("piece")
                )
        );
    }

}
