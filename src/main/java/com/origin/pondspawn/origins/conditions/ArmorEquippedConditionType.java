package com.origin.pondspawn.origins.conditions;

import com.origin.pondspawn.PondspawnOrigin;
import com.origin.pondspawn.origins.init.ModConditionTypes;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.context.EntityConditionContext;
import io.github.apace100.apoli.condition.type.EntityConditionType;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class ArmorEquippedConditionType extends EntityConditionType {

    private final String piece;

    public static final TypedDataObjectFactory<ArmorEquippedConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
            new SerializableData()
                    .add("piece",SerializableDataTypes.STRING),
            data -> new ArmorEquippedConditionType(
                    data.get("piece")
            ),
            (conditionType,serializableData) -> serializableData.instance()
                    .set("piece",conditionType.piece)

    );

    public ArmorEquippedConditionType(String piece) {
        this.piece = piece;
    }

    @Override
    public boolean test(EntityConditionContext context) {
        Entity entity = context.entity();

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

    @Override
    public @NotNull ConditionConfiguration<?> getConfig() {
        return ModConditionTypes.ARMOR_EQUIPPED;
    }


}
