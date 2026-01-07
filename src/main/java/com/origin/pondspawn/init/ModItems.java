package com.origin.pondspawn.init;

import com.origin.pondspawn.PondspawnOrigin;
import com.origin.pondspawn.list.FoodList;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item CALDO_CANA = register("caldo_cana", new Item(new Item.Settings()
            .food(FoodList.CALDO_CANA_COMPONENT)
            .maxCount(16)
    ));

    public static <T extends Item> T register(String name, T item) {
        return Registry.register(Registries.ITEM, PondspawnOrigin.id(name),item);
    }

    public static void load() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK)
           .register((itemGroup) -> itemGroup.add(ModItems.CALDO_CANA));
    }
}
