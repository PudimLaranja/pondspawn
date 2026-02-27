package com.origin.pondspawn.origins.entityactions;

import com.origin.pondspawn.origins.init.ModActionTypes;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.data.TypedDataObjectFactory;
import io.github.apace100.apoli.power.PowerReference;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.util.PowerUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class ThirstHandlerActionType extends EntityActionType {

    private static final SerializableData THIRST_CONDITIONS_DATA = new SerializableData()
            .add("value", SerializableDataTypes.INT)
            .add("condition", EntityCondition.DATA_TYPE);

    public static final TypedDataObjectFactory<ThirstHandlerActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
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
                        List.of()),
            data -> new ThirstHandlerActionType(
                    data.get("resource"),
                    data.get("reduce_on"),
                    data.get("increase_on")
            ),
            (actionType,serializableData) -> serializableData.instance()
                    .set("resource",actionType.resource)
                    .set("reduce_on",actionType.reduceConditions)
                    .set("increase_on",actionType.increaseConditions)

    );



    private final PowerReference resource;
    private final List<SerializableData.Instance> reduceConditions;
    private final List<SerializableData.Instance> increaseConditions;

    public ThirstHandlerActionType(PowerReference resource, List<SerializableData.Instance> reduceConditions, List<SerializableData.Instance> increaseConditions) {
        this.resource = resource;
        this.reduceConditions = reduceConditions;
        this.increaseConditions = increaseConditions;
    }


    @Override
    public void accept(EntityActionContext context) {

        Entity entity = context.entity();
        PowerType powerType = resource.getNullablePowerType(entity);

        int oldValue;
        int newValue;


        oldValue = PowerUtil.getResourceValue(powerType);
        newValue = oldValue;


//        for (SerializableData.Instance data: increaseConditions) {
//
//            int increase = data.getInt("value");
//
//            Predicate<Entity> condition = data.get("condition");
//
//            newValue = changeThirst(newValue,condition.test(entity),increase);
//
//        }
//
//        for (SerializableData.Instance data: reduceConditions) {
//
//            int reduce = data.getInt("value");
//
//            Predicate<Entity> condition = data.get("condition");
//
//            newValue = changeThirst(newValue,condition.test(entity),-reduce);
//
//        }

        PowerUtil.setResourceValue(powerType,newValue);
        if (oldValue != newValue) {
            PowerHolderComponent.syncPower(entity,resource);
        }


    }

    private static int changeThirst(int oldValue, boolean condition, int value) {
        int decrement = condition ? value : 0;

        return oldValue + decrement;
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ModActionTypes.THIRST_HANDLER;
    }

    //

}
