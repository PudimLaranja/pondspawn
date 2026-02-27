package com.origin.pondspawn.origins.init;

import com.origin.pondspawn.PondspawnOrigin;
import com.origin.pondspawn.origins.conditions.ArmorEquippedConditionType;
import io.github.apace100.apoli.condition.ConditionConfiguration;
import io.github.apace100.apoli.condition.type.EntityConditionType;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class ModConditionTypes {

    public static final ConditionConfiguration<ArmorEquippedConditionType> ARMOR_EQUIPPED =
            register(ConditionConfiguration.of(PondspawnOrigin.id("armor_equipped"),ArmorEquippedConditionType.DATA_FACTORY));

    @SuppressWarnings("unchecked")
    public static <CT extends EntityConditionType> ConditionConfiguration<CT> register(ConditionConfiguration<CT> configuration) {

        ConditionConfiguration<EntityConditionType> casted = (ConditionConfiguration<EntityConditionType>) configuration;
        Registry.register(ApoliRegistries.ENTITY_CONDITION_TYPE, casted.id(), casted);

        return configuration;
    }
    public static void register() {}
}