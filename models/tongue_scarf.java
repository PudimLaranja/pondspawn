// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package com.example.mod;
   
public class tongue_scarf extends EntityModel<Entity> {
	private final ModelPart bb_main;
	public tongue_scarf(ModelPart root) {
		this.bb_main = root.getChild("bb_main");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(-1, 0).cuboid(-7.0F, 0.0F, 2.0F, 14.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(17, 16).cuboid(-7.2F, 1.7F, -2.8F, 2.0F, 1.0F, 5.0F, new Dilation(0.0F))
		.uv(0, 3).cuboid(-6.0F, 0.0F, -3.0F, 12.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(17, 23).cuboid(5.6F, 1.7F, -2.6F, 2.0F, 1.0F, 5.0F, new Dilation(0.0F))
		.uv(14, 29).cuboid(7.0F, 0.0F, -2.0F, 1.0F, 2.0F, 4.0F, new Dilation(0.0F))
		.uv(23, 28).cuboid(-7.5F, 0.0F, -3.0F, 1.0F, 2.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData cube_r1 = bb_main.addChild("cube_r1", ModelPartBuilder.create().uv(-1, 12).cuboid(-7.2548F, -0.8335F, 0.0F, 15.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, -3.6F, 0.0F, 0.0F, -0.096F));

		ModelPartData cube_r2 = bb_main.addChild("cube_r2", ModelPartBuilder.create().uv(0, 15).cuboid(-3.1117F, -1.1789F, -2.6052F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, -4.0F, -4.0F, 0.3927F, 0.0F, 1.6581F));

		ModelPartData cube_r3 = bb_main.addChild("cube_r3", ModelPartBuilder.create().uv(26, 8).cuboid(-0.1908F, 0.0F, -0.9816F, 6.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.9F, -4.0F, -4.8F, 3.0805F, -0.192F, 1.6581F));

		ModelPartData cube_r4 = bb_main.addChild("cube_r4", ModelPartBuilder.create().uv(26, 0).cuboid(5.0F, 0.0F, -5.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(-12.5F, -3.0F, 1.8F, 0.0F, 0.0F, 0.0873F));

		ModelPartData cube_r5 = bb_main.addChild("cube_r5", ModelPartBuilder.create().uv(0, 23).cuboid(-0.0681F, -0.5604F, -3.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(7.0F, -0.9F, 0.0F, 0.0F, 0.0F, 0.192F));

		ModelPartData cube_r6 = bb_main.addChild("cube_r6", ModelPartBuilder.create().uv(-1, 9).cuboid(-2.3641F, -1.873F, 0.0F, 15.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-5.0F, -0.4F, -3.3F, 0.0F, 0.0F, 0.2094F));

		ModelPartData cube_r7 = bb_main.addChild("cube_r7", ModelPartBuilder.create().uv(-1, 6).cuboid(-7.0F, 0.0F, 0.0F, 15.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 2.2F, -0.1745F, 0.0F, 0.0873F));
		return TexturedModelData.of(modelData, 64, 64);
	}
	@Override
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		bb_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}