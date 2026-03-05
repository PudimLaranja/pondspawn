package com.origin.pondspawn.origins.init;

import com.origin.pondspawn.PondspawnOrigin;
import com.origin.pondspawn.origins.powertypes.ModifyLungCapacityPowerType;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.power.PowerConfiguration;
import io.github.apace100.apoli.power.type.ModifyAirSpeedPowerType;
import io.github.apace100.apoli.power.type.PowerType;
import io.github.apace100.apoli.power.type.ValueModifyingPowerType;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.registry.Registry;

public class ModPowerTypes {

    public static final PowerConfiguration<ModifyLungCapacityPowerType> MODIFY_LUNG_CAPACITY = register(
        ValueModifyingPowerType.createModifyingConfiguration(PondspawnOrigin.id("modify_lung_capacity"), ModifyLungCapacityPowerType::new)
    );

    public static void register() {}

    @SuppressWarnings("unchecked")
    public static <T extends PowerType> PowerConfiguration<T> register(PowerConfiguration<T> configuration) {

        PowerConfiguration<PowerType> casted = (PowerConfiguration<PowerType>) configuration;
        Registry.register(ApoliRegistries.POWER_TYPE, casted.id(), casted);

        return configuration;

    }
}
