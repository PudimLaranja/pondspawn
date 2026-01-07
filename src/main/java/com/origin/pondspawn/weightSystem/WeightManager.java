package com.origin.pondspawn.weightSystem;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.origin.pondspawn.PondspawnOrigin;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
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

    public static double getWeight(EntityType<?> type) {
        return WEIGHT_MAP.getOrDefault(type, 1.0d); // Default weight is 1.0
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
