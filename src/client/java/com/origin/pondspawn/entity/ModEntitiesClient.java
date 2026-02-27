package com.origin.pondspawn.entity;

import com.origin.pondspawn.PondspawnOrigin;
import com.origin.pondspawn.entity.model.TongueModel;
import com.origin.pondspawn.entity.model.TongueTipModel;
import com.origin.pondspawn.entity.renderer.TongueRenderer;
import com.origin.pondspawn.entity.renderer.TongueTipRenderer;
import com.origin.pondspawn.init.ModEntities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class ModEntitiesClient {

    public static final EntityModelLayer TONGUE_MODEL_LAYER = new EntityModelLayer(
            PondspawnOrigin.id("tongue"),
            "main"
    );

    public static final EntityModelLayer TONGUE_TIP_MODEL_LAYER = new EntityModelLayer(
            PondspawnOrigin.id("tongue_tip"),
            "main"
    );

    public static void load() {
        EntityRendererRegistry.register(
                ModEntities.TONGUE_ENTITY_TYPE,
                TongueRenderer::new
        );

        EntityRendererRegistry.register(
                ModEntities.TONGUE_TIP_ENTITY_TYPE,
                TongueTipRenderer::new
        );

        EntityModelLayerRegistry.registerModelLayer(
                TONGUE_MODEL_LAYER,
                TongueModel::getTexturedModelData
        );

        EntityModelLayerRegistry.registerModelLayer(
                TONGUE_TIP_MODEL_LAYER,
                TongueTipModel::getTexturedModelData
        );

    }
}
