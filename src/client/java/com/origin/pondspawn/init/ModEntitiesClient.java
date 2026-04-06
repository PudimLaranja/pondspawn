package com.origin.pondspawn.init;

import com.origin.pondspawn.PondspawnOrigin;
import com.origin.pondspawn.entity.model.TongueModel;
import com.origin.pondspawn.entity.model.TongueScarfModel;
import com.origin.pondspawn.entity.model.TongueScarfMouthModel;
import com.origin.pondspawn.entity.model.TongueTipModel;
import com.origin.pondspawn.entity.renderer.TongueRenderer;
import com.origin.pondspawn.entity.renderer.TongueScarfMouthRenderer;
import com.origin.pondspawn.entity.renderer.TongueScarfRenderer;
import com.origin.pondspawn.entity.renderer.TongueTipRenderer;
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

    public static final EntityModelLayer TONGUE_SCARF_MODEL_LAYER = new EntityModelLayer(
            PondspawnOrigin.id("tongue_scarf"),
            "main"
    );

    public static final EntityModelLayer TONGUE_SCARF_MOUTH_MODEL_LAYER = new EntityModelLayer(
            PondspawnOrigin.id("tongue_scarf_mouth"),
      "main"
    );

    public static void load() {
        EntityRendererRegistry.register(
                ModEntityTypes.TONGUE_ENTITY_TYPE,
                TongueRenderer::new
        );

        EntityRendererRegistry.register(
                ModEntityTypes.TONGUE_TIP_ENTITY_TYPE,
                TongueTipRenderer::new
        );

        EntityRendererRegistry.register(
                ModEntityTypes.TONGUE_SCARF_ENTITY_TYPE,
                TongueScarfRenderer::new
        );

        EntityRendererRegistry.register(
                ModEntityTypes.TONGUE_SCARF_MOUTH_ENTITY_TYPE,
                TongueScarfMouthRenderer::new
        );

        EntityModelLayerRegistry.registerModelLayer(
                TONGUE_MODEL_LAYER,
                TongueModel::getTexturedModelData
        );

        EntityModelLayerRegistry.registerModelLayer(
                TONGUE_TIP_MODEL_LAYER,
                TongueTipModel::getTexturedModelData
        );

        EntityModelLayerRegistry.registerModelLayer(
                TONGUE_SCARF_MODEL_LAYER,
                TongueScarfModel::getTexturedModelData
        );

        EntityModelLayerRegistry.registerModelLayer(
                TONGUE_SCARF_MOUTH_MODEL_LAYER,
                TongueScarfMouthModel::getTexturedModelData
        );

    }
}
