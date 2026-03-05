package com.origin.pondspawn.mixin;

import com.origin.pondspawn.origins.powertypes.ModifyLungCapacityPowerType;
import io.github.apace100.apoli.component.PowerHolderComponent;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityPowersMixin {

    @Inject(method = "getMaxAir", at = @At("RETURN"), cancellable = true)
    private void modifyAirCapacity(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Math.round(PowerHolderComponent.modify((Entity) (Object) this, ModifyLungCapacityPowerType.class,cir.getReturnValue())));
    }

}
