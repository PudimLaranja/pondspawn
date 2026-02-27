package com.origin.pondspawn.entity.renderer;

import com.origin.pondspawn.PondspawnOrigin;
import com.origin.pondspawn.entity.ModEntitiesClient;
import com.origin.pondspawn.entity.custum.TongueTip;
import com.origin.pondspawn.entity.model.TongueModel;
import com.origin.pondspawn.entity.model.TongueTipModel;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class TongueTipRenderer extends EntityRenderer<TongueTip> {

    private final TongueTipModel model;
    public static final Identifier TEXTURE = PondspawnOrigin.id("/textures/entity/tongue_tip_texture.png");

    public TongueTipRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new TongueTipModel(ctx.getPart(ModEntitiesClient.TONGUE_TIP_MODEL_LAYER));
        this.shadowRadius = 0.2f;
    }

    @Override
    public Identifier getTexture(TongueTip entity) {
        return TEXTURE;
    }
    @Override
    public TextRenderer getTextRenderer() {
        return super.getTextRenderer();
    }

    @Override
    public void render(TongueTip entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        this.model.render(
                matrices,
                vertexConsumers.getBuffer(this.model.getLayer(TEXTURE)),
                255,
                OverlayTexture.DEFAULT_UV,
                0xFFFFFFFF
        );

    }
}
