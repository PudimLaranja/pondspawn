package com.origin.pondspawn.list;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;

public class FoodList {
    public static final FoodComponent CALDO_CANA_COMPONENT = new FoodComponent.Builder()
            .nutrition(6)
            .saturationModifier(1F)
            .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 20 * 2,1),1.0F)
            .usingConvertsTo(() -> Items.GLASS_BOTTLE)
            .build();
}
