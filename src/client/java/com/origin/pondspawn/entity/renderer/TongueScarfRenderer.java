package com.origin.pondspawn.entity.renderer;

import com.origin.pondspawn.PlayerWithTongueData;
import com.origin.pondspawn.PondspawnOrigin;
import com.origin.pondspawn.init.ModEntitiesClient;
import com.origin.pondspawn.entity.custum.TongueScarf;
import com.origin.pondspawn.entity.model.TongueScarfModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TongueScarfRenderer extends EntityRenderer<TongueScarf> {

    private static final Logger log = LoggerFactory.getLogger(TongueScarfRenderer.class);
    private final TongueScarfModel model;
    public static final Identifier TEXTURE = PondspawnOrigin.id("/textures/entity/tongue_scarf_texture.png");

    public TongueScarfRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new TongueScarfModel(ctx.getPart(ModEntitiesClient.TONGUE_SCARF_MODEL_LAYER));
        this.shadowRadius = 0.0f;
    }

    @Override
    public Identifier getTexture(TongueScarf entity) {
        return TEXTURE;
    }

    @Override
    public TextRenderer getTextRenderer() {
        return super.getTextRenderer();
    }


    @Override
    public void render(TongueScarf entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

        matrices.push();

        if (entity.getVehicle() instanceof PlayerEntity player) {
            MinecraftClient client = MinecraftClient.getInstance();

            PlayerWithTongueData tongueData = (PlayerWithTongueData) player;

            float scale = 0.66f;

            matrices.scale(scale,scale,scale);

            if (
                    (
                        client.player instanceof ClientPlayerEntity clientPlayer &&
                        clientPlayer.getUuid() == player.getUuid() &&
                        client.options.getPerspective().isFirstPerson()
                    ) ||
                        ((PlayerWithTongueData) player).pondspawn$isTongueOut()
            ) {

                matrices.scale(0,0,0);
            }



            float bodyRotation = player.bodyYaw;

            matrices.multiply(
                    RotationAxis.POSITIVE_Y
                            .rotationDegrees(-bodyRotation + 180)
            );

            if (player.isSneaking()) {
                matrices.translate(0,-1.9f,0);
            } else {
                matrices.translate(0,-2.0f,0);
            }

        }



        this.model.render(
                matrices,
                vertexConsumers.getBuffer(this.model.getLayer(TEXTURE)),
                light,
                OverlayTexture.DEFAULT_UV,
                0xFFFFFFFF
        );

        matrices.pop();
    }
}
