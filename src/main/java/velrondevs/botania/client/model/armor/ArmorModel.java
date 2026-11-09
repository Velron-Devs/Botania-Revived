package velrondevs.botania.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;

public class ArmorModel extends HumanoidModel<HumanoidRenderState> {
	protected final EquipmentSlot slot;

	public ArmorModel(ModelPart root, EquipmentSlot slot) {
		super(root);
		this.slot = slot;
	}

	@Override
	public void setupAnim(HumanoidRenderState state) {
		super.setupAnim(state);
		if (state instanceof ArmorStandRenderState stand) {
			float f = (float) Math.PI / 180F;
			this.head.xRot = f * stand.headPose.x();
			this.head.yRot = f * stand.headPose.y();
			this.head.zRot = f * stand.headPose.z();
			this.body.xRot = f * stand.bodyPose.x();
			this.body.yRot = f * stand.bodyPose.y();
			this.body.zRot = f * stand.bodyPose.z();
			this.leftArm.xRot = f * stand.leftArmPose.x();
			this.leftArm.yRot = f * stand.leftArmPose.y();
			this.leftArm.zRot = f * stand.leftArmPose.z();
			this.rightArm.xRot = f * stand.rightArmPose.x();
			this.rightArm.yRot = f * stand.rightArmPose.y();
			this.rightArm.zRot = f * stand.rightArmPose.z();
			this.leftLeg.xRot = f * stand.leftLegPose.x();
			this.leftLeg.yRot = f * stand.leftLegPose.y();
			this.leftLeg.zRot = f * stand.leftLegPose.z();
			this.rightLeg.xRot = f * stand.rightLegPose.x();
			this.rightLeg.yRot = f * stand.rightLegPose.y();
			this.rightLeg.zRot = f * stand.rightLegPose.z();
		}
		setPartVisibility(slot);
	}

	private void setPartVisibility(EquipmentSlot slot) {
		head.visible = false;
		hat.visible = false;
		body.visible = false;
		rightArm.visible = false;
		leftArm.visible = false;
		rightLeg.visible = false;
		leftLeg.visible = false;
		switch (slot) {
			case HEAD -> {
				head.visible = true;
				hat.visible = true;
			}
			case CHEST -> {
				body.visible = true;
				rightArm.visible = true;
				leftArm.visible = true;
			}
			case LEGS -> {
				body.visible = true;
				rightLeg.visible = true;
				leftLeg.visible = true;
			}
			case FEET -> {
				rightLeg.visible = true;
				leftLeg.visible = true;
			}
			default -> {
			}
		}
	}
}
