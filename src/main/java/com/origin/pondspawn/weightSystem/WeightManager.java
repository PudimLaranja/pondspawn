package com.origin.pondspawn.weightSystem;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.origin.pondspawn.PondspawnOrigin;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profiler;

import java.util.HashMap;
import java.util.Map;

public class WeightManager extends JsonDataLoader implements IdentifiableResourceReloadListener {

    private static final Map<EntityType<?>,Double> WEIGHT_MAP = new HashMap<>();
    private static final Gson GSON = new Gson();
    private final Identifier id;

    public WeightManager() {
        super(GSON,"weights");
        this.id = PondspawnOrigin.id("weight_loader");
    }

    @Override
    public Identifier getFabricId() {
        return this.id;
    }

    public static double getWeight(Entity entity) {
        double weight = WEIGHT_MAP.getOrDefault(entity.getType(), 1.0d);

        if (entity instanceof LivingEntity living) {
            double multiply = (double) living.getArmor() / 5;
            weight *= Math.max(multiply,1.0);

            for (ItemStack stack: living.getArmorItems()) {
                if (!stack.isEmpty() && stack.getItem() instanceof ArmorItem armorItem) {
                    if (armorItem.getMaterial().value().equals(ArmorMaterials.NETHERITE.value())) {
                        weight += 5.0;
                    }
                }
            }
        }

        if (entity instanceof SlimeEntity slime) {
            weight *= slime.getSize() + 1;
        }

        if (entity instanceof MagmaCubeEntity magmaCube) {
            weight *= magmaCube.getSize() + 1;
        }

        return Math.max(1.0,weight); // Default weight is 1.0
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        WEIGHT_MAP.clear();

        prepared.forEach((id, element) -> {
            if (element.isJsonObject()) {
                JsonObject jsonObject = element.getAsJsonObject();
                for (String key : jsonObject.keySet()) {
                    Identifier entityId = Identifier.of(key);
                    EntityType<?> type = Registries.ENTITY_TYPE.get(entityId);

                    double weight = jsonObject.get(key).getAsDouble();
                    WEIGHT_MAP.put(type, weight);
                }
            }
        });
    }
}
