package com.origin.pondspawn.origins.entityactions;

import com.origin.pondspawn.origins.init.ModActionTypes;
import io.github.apace100.apoli.action.ActionConfiguration;
import io.github.apace100.apoli.action.context.EntityActionContext;
import io.github.apace100.apoli.action.type.EntityActionType;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.condition.context.EntityConditionContext;
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

public class ThirstHandlerActionType extends EntityActionType {

    // 1. Criamos um record para armazenar as duplas (Valor + Condição) de forma limpa
    public record ThirstModifier(int value, EntityCondition condition) {}

    // 2. Declaramos como o Minecraft deve ler esse record a partir do JSON
    public static final SerializableDataType<ThirstModifier> THIRST_MODIFIER_DATA_TYPE = SerializableDataType.compound(
            new SerializableData()
                    .add("value", SerializableDataTypes.INT)
                    .add("condition", EntityCondition.DATA_TYPE), // NOVO: Uso de EntityCondition.DATA_TYPE
            data -> new ThirstModifier(
                    data.get("value"),
                    data.get("condition")
            ),
            (modifier, serializableData) -> serializableData.instance()
                    .set("value", modifier.value())
                    .set("condition", modifier.condition())
    );

    // 3. Atualizamos a Factory usando o nosso novo tipo de dado em lista
    public static final TypedDataObjectFactory<ThirstHandlerActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
            new SerializableData()
                    .add("resource", ApoliDataTypes.POWER_REFERENCE)
                    .add("reduce_on", THIRST_MODIFIER_DATA_TYPE.list(), List.of())
                    .add("increase_on", THIRST_MODIFIER_DATA_TYPE.list(), List.of()),
            data -> new ThirstHandlerActionType(
                    data.get("resource"),
                    data.get("reduce_on"),
                    data.get("increase_on")
            ),
            (actionType, serializableData) -> serializableData.instance()
                    .set("resource", actionType.resource)
                    .set("reduce_on", actionType.reduceConditions)
                    .set("increase_on", actionType.increaseConditions)
    );


    // 4. Os atributos da classe agora usam nossa estrutura limpa
    private final PowerReference resource;
    private final List<ThirstModifier> reduceConditions;
    private final List<ThirstModifier> increaseConditions;

    public ThirstHandlerActionType(PowerReference resource, List<ThirstModifier> reduceConditions, List<ThirstModifier> increaseConditions) {
        this.resource = resource;
        this.reduceConditions = reduceConditions;
        this.increaseConditions = increaseConditions;
    }

    @Override
    public void accept(EntityActionContext context) {

        Entity entity = context.entity();
        PowerType powerType = resource.getNullablePowerType(entity);

        if (powerType == null) return; // Checagem de segurança opcional, mas recomendada

        // Extraímos o contexto de condição do contexto de ação!
        EntityConditionContext conditionContext = context.forCondition();

        int oldValue = PowerUtil.getResourceValue(powerType);
        int newValue = oldValue;

        // Iteramos sobre os aumentos
        for (ThirstModifier modifier : increaseConditions) {
            if (modifier.condition().test(conditionContext)) {
                newValue += modifier.value();
            }
        }

        // Iteramos sobre as reduções
        for (ThirstModifier modifier : reduceConditions) {
            if (modifier.condition().test(conditionContext)) {
                newValue -= modifier.value();
            }
        }

        // Aplicamos a mudança caso tenha ocorrido
        if (oldValue != newValue) {
            PowerUtil.setResourceValue(powerType, newValue);
            PowerHolderComponent.syncPower(entity, resource);
        }
    }

    @Override
    public @NotNull ActionConfiguration<?> getConfig() {
        return ModActionTypes.THIRST_HANDLER; // Certifique-se de que isso esteja chamando sua configuração registrada
    }

}