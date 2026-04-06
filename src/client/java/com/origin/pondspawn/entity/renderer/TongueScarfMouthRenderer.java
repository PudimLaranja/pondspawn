package com.origin.pondspawn.entity.renderer;

import com.origin.pondspawn.PlayerWithTongueData;
import com.origin.pondspawn.PondspawnOrigin;
import com.origin.pondspawn.init.ModEntitiesClient;
import com.origin.pondspawn.entity.custum.TongueScarf;
import com.origin.pondspawn.entity.custum.TongueScarfMouth;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TongueScarfMouthRenderer extends EntityRenderer<TongueScarfMouth> {

    private static final Logger log = LoggerFactory.getLogger(TongueScarfMouthRenderer.class);
    private final TongueScarfModel model;
    public static final Identifier TEXTURE = PondspawnOrigin.id("/textures/entity/tongue_scarf_mouth_texture.png");

    public TongueScarfMouthRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new TongueScarfModel(ctx.getPart(ModEntitiesClient.TONGUE_SCARF_MOUTH_MODEL_LAYER));
        this.shadowRadius = 0.0f;
    }

    @Override
    public Identifier getTexture(TongueScarfMouth entity) {
        return TEXTURE;
    }

    @Override
    public TextRenderer getTextRenderer() {
        return super.getTextRenderer();
    }


    @Override
    public void render(TongueScarfMouth entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        if (entity.getVehicle() instanceof TongueScarf scarf && scarf.getVehicle() instanceof PlayerEntity player) {
            // 1. Match the player's body/head yaw
            // We use lerp (MathHelper.lerp) to ensure smooth rotation between ticks
            float headYaw = MathHelper.lerp(tickDelta, player.prevHeadYaw, player.headYaw);
            float pitch = MathHelper.lerp(tickDelta, player.prevPitch, player.getPitch());

            MinecraftClient client = MinecraftClient.getInstance();
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

            matrices.translate(0.0,-0.75,0.0);


            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f - (headYaw + 180f)));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch + 180f));

            float scale = 0.66f;

            matrices.scale(scale,scale,scale);

            // Optional: If the mouth is offset from the center of the player's head,
            // add a small translation here:
            // matrices.translate(0.0, 1.5, 0.0);
        }

        this.model.render(
                matrices,
                vertexConsumers.getBuffer(this.model.getLayer(TEXTURE)),
                light,
                OverlayTexture.DEFAULT_UV,
                0xFFFFFFFF
        );

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
}
