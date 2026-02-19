package com.origin.pondspawn.origins.entityactions;

import com.origin.pondspawn.PondspawnOrigin;
import io.github.apace100.apoli.action.factory.ActionTypeFactory;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerReference;
import io.github.apace100.apoli.power.type.VariableIntPowerType;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;
import java.util.function.Predicate;

public class ThirstHandlerActionType {

    private static final SerializableData THIRST_CONDITIONS_DATA = new SerializableData()
            .add("value", SerializableDataTypes.INT)
            .add("condition",ApoliDataTypes.ENTITY_CONDITION);

    public static void action(Entity entity, PowerReference power, List<SerializableData.Instance> reduce_conditions,List<SerializableData.Instance> increase_conditions) {

        int oldValue;
        int newValue;

        switch (power.getType(entity)) {
            case VariableIntPowerType resource -> {

                oldValue = resource.getValue();
                newValue = oldValue;


                for (SerializableData.Instance data: increase_conditions) {

                    int increase = data.getInt("value");

                    Predicate<Entity> condition = data.get("condition");

                    newValue = changeThirst(newValue,condition.test(entity),increase);

                }

                for (SerializableData.Instance data: reduce_conditions) {

                    int reduce = data.getInt("value");

                    Predicate<Entity> condition = data.get("condition");

                    newValue = changeThirst(newValue,condition.test(entity),-reduce);

                }

                resource.setValue(
                        Math.clamp(newValue,resource.getMin(),resource.getMax())
                );
            }
            case null, default -> throw new IllegalStateException("Unexpected value: " + power.getType(entity));
        }
        if (oldValue != newValue) {


            PowerHolderComponent.syncPower(entity,power);
        }


    }


    private static int changeThirst(int oldValue, boolean condition, int value) {
        int decrement = condition ? value : 0;

        return oldValue + decrement;
    }

    public static ActionTypeFactory<Entity> getFactory() {
        return new ActionTypeFactory<>(
                PondspawnOrigin.id("thirst_handler"),
                new SerializableData()
                        .add("resource", ApoliDataTypes.POWER_REFERENCE)
                        .add("reduce_on", SerializableDataType.list(
                                SerializableDataType.compound(
                                        THIRST_CONDITIONS_DATA,
                                        (instance) -> instance,
                                        (instance,data) -> instance
                                )
                            ),
                                List.of()
                        )
                        .add("increase_on", SerializableDataType.list(
                                SerializableDataType.compound(
                                        THIRST_CONDITIONS_DATA,
                                        (instance) -> instance,
                                        (instance,data) -> instance
                                )
                            ),
                        List.of())
                ,

                (data, entity) -> action(
                        entity,
                        data.get("resource"),
                        data.get("reduce_on"),
                        data.get("increase_on")
                )
        );
    }

}
