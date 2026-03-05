package com.origin.pondspawn.origins.powertypes;

import com.origin.pondspawn.origins.init.ModPowerTypes;
import io.github.apace100.apoli.condition.EntityCondition;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.ValueModifyingPowerType;
import io.github.apace100.apoli.util.modifier.Modifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ModifyLungCapacityPowerType extends ValueModifyingPowerType {

    public ModifyLungCapacityPowerType(List<Modifier> modifiers, Optional<EntityCondition> condition) {
        super(modifiers,condition);
    }

    @Override
    public @NotNull PowerConfiguration<?> getConfig() {
        return ModPowerTypes.MODIFY_LUNG_CAPACITY;
    }
}
