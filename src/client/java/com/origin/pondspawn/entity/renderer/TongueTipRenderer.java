package com.origin.pondspawn.entity.renderer;

import com.origin.pondspawn.PondspawnOrigin;
import com.origin.pondspawn.init.ModEntitiesClient;
import com.origin.pondspawn.entity.custum.Tongue;
import com.origin.pondspawn.entity.custum.TongueTip;
import com.origin.pondspawn.entity.model.TongueTipModel;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class TongueTipRenderer extends EntityRenderer<TongueTip> {

    private static final Logger log = LoggerFactory.getLogger(TongueTipRenderer.class);
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


        matrices.push();


        UUID parentUuid = entity.getParentUUID();
        World world = entity.getWorld();


        if (parentUuid != null && world instanceof ClientWorld clientWorld) {
            if (Common.getEntityByUuid(clientWorld, parentUuid) instanceof Tongue tongue) {


                if (Common.getEntityByUuid(clientWorld, tongue.getEntityTarget()) instanceof PlayerEntity player) {

                    Vec3d mouthPos = Common.getPlayerMouthPosition(player, tickDelta);
                    Vec3d tipPos = entity.getLerpedPos(tickDelta);

                    float animation = 1f - tongue.getAnimationController();

                    Vec3d animationOffset = mouthPos.subtract(tipPos).multiply(animation);

                    matrices.translate(
                            animationOffset.x,
                            animationOffset.y,
                            animationOffset.z
                    );

                }

            }
        }


        this.model.render(
                matrices,
                vertexConsumers.getBuffer(this.model.getLayer(TEXTURE)),
                255,
                OverlayTexture.DEFAULT_UV,
                0xFFFFFFFF
        );

        matrices.pop();

    }
}
