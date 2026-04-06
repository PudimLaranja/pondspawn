package com.origin.pondspawn.entity.renderer;

import com.origin.pondspawn.PondspawnOrigin;
import com.origin.pondspawn.entity.custum.Tongue;
import com.origin.pondspawn.init.ModEntitiesClient;
import com.origin.pondspawn.entity.model.TongueModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


public class TongueRenderer extends EntityRenderer<Tongue> {



    public static final Identifier TEXTURE = PondspawnOrigin.id("/textures/entity/tongue_texture.png");
    private static final Logger log = LoggerFactory.getLogger(TongueRenderer.class);

    private final TongueModel model;


    public TongueRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);

        this.model = new TongueModel(ctx.getPart(ModEntitiesClient.TONGUE_MODEL_LAYER));
        this.shadowRadius = 0.0f;
    }

    @Override
    public Identifier getTexture(Tongue entity) {
        return TEXTURE;
    }

    @Override
    public TextRenderer getTextRenderer() {
        return super.getTextRenderer();
    }

    private MatrixStack animationHandler(MatrixStack matrices, double distance, float animationControl) {


        float offset = (float) distance * (1f - animationControl);

        matrices.translate(0, 0, offset);


        matrices.scale(1.0f,1.0f,(float) distance * animationControl);

        return matrices;
    }


    @Override
    public void render(Tongue entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {

        Vec3d entityPos = entity.getPos();

        ClientWorld clientWorld = (ClientWorld) entity.getWorld();

        Vec3d targetCoordinate = entity.getPos().add(new Vec3d(0.0, 1.0, 0.0));


        if (clientWorld != null) {
            UUID entityId = entity.getEntityTarget();
            Entity targetEntity = null;


            for (Entity currentEntity: clientWorld.getEntities()) {
                if(currentEntity.getUuid().equals(entityId)) {
                    targetEntity = currentEntity;
                    break;
                }
            }



            if (targetEntity != null) {

                if (targetEntity instanceof PlayerEntity player) {

                    MinecraftClient client = MinecraftClient.getInstance();
                    if (
                        client.player instanceof ClientPlayerEntity clientPlayer &&
                        clientPlayer.getUuid() == player.getUuid() &&
                        client.options.getPerspective().isFirstPerson() && false
                    ) {
                        Vec3d mouthPosition = Common.getPlayerMouthPosition(player,tickDelta);
                        Vec3d dir = entityPos.subtract(mouthPosition).normalize();
                        targetCoordinate = mouthPosition.add(dir.multiply(0.4));
                    } else {
                        targetCoordinate = Common.getPlayerMouthPosition(player,tickDelta);
                    }



                }
            }
        }


        matrices.push();

        //Vector3f directionToTarget = to_Vector3f(targetCoordinate.subtract(entityPos));
        Vec3d directionToTarget = targetCoordinate.subtract(entityPos);
        double distance = directionToTarget.length();
        directionToTarget.normalize();

        float targetYaw = (float) MathHelper.atan2(directionToTarget.z,directionToTarget.x);
        targetYaw -= (float) Math.PI / 2.0F;

        double horizontalDistance = Math.sqrt(
                directionToTarget.x * directionToTarget.x +
                        directionToTarget.z * directionToTarget.z
        );

        float targetPitch = (float) MathHelper.atan2(
                directionToTarget.y,horizontalDistance
        );

        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(-targetYaw));
        matrices.multiply(RotationAxis.POSITIVE_X.rotation(-targetPitch));

        float animationControl = entity.getAnimationController();

        matrices = animationHandler(matrices,distance,animationControl);

        //rotation.rotationTo(MODEL_FORWARD_VECTOR,directionToTarget);

        //matrices.multiply(rotation);

//        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
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
