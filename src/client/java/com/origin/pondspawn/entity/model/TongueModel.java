package com.origin.pondspawn.entity.model;

import com.origin.pondspawn.entity.custum.Tongue;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

// Made with Blockbench 5.0.4
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class TongueModel extends EntityModel<Tongue> {
    private final ModelPart bb_main;
    public TongueModel(ModelPart root) {
        this.bb_main = root.getChild("bb_main");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData bb_main = modelPartData.addChild(
                "bb_main",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(
                                -1.0F, 1.0F, 0.0F,
                                2.0F, 2.0F, 16.0F,
                                new Dilation(0.0F)
                        ),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(Tongue entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }



    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        bb_main.render(matrices,vertices,light,overlay,color);
    }

}
