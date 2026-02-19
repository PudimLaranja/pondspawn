package com.origin.pondspawn.origins.init;

import com.origin.pondspawn.origins.conditions.ArmorEquippedCondition;
import io.github.apace100.apoli.condition.factory.ConditionTypeFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class ModConditions {

    public static void register() {
       register(ArmorEquippedCondition.getFactory());
    }

    public static <F extends ConditionTypeFactory<Entity>> F register(F conditionFactory) {
        return Registry.register(ApoliRegistries.ENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
