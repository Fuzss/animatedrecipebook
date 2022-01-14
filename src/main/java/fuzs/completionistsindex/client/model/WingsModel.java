package fuzs.completionistsindex.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WingsModel<T extends LivingEntity> extends AgeableListModel<T> {
   private final ModelPart rightWing;
   private final ModelPart leftWing;

   public WingsModel(ModelPart p_170538_) {
      this.leftWing = p_170538_.getChild("left_wing");
      this.rightWing = p_170538_.getChild("right_wing");
   }

   public static LayerDefinition createLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();
      CubeDeformation cubedeformation = CubeDeformation.NONE;
//      partdefinition.addOrReplaceChild("left_wing",  CubeListBuilder.create().texOffs(35, 2).addBox(0.0F, -12.0F, 0.0F, 10.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 2.0F, 0.0F, -0.6109F, 0.0F));
//      partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(35, 2).mirror().addBox(-10.0F, -12.0F, 0.0F, 10.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 2.0F, 0.0F, 0.6109F, 0.0F));
//      partdefinition.addOrReplaceChild("left_wing",  CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -8.0F, 0.0F, 10.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 2.0F, 0.0F, -2.5307F, 0.0F));
//      partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -12.0F, 0.0F, 10.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 2.0F, 0.0F, 2.5307F, 0.0F));
//      partdefinition.addOrReplaceChild("left_wing",  CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-16.0F, -14.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 12.0F, 2.0F, 0.0F, 2.5307F, 0.0F));
//      partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -10.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 8.0F, 2.0F, 0.0F, -2.5307F, 0.0F));
      partdefinition.addOrReplaceChild("left_wing",  CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-16.0F, -14.0F, 0.0F, 16.0F, 16.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 12.0F, 1.0F, 0.0F, 2.5307F, 0.0F));
      partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -10.0F, 0.0F, 16.0F, 16.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 8.0F, 1.0F, 0.0F, -2.5307F, 0.0F));
      return LayerDefinition.create(meshdefinition, 32, 16);
   }

   protected Iterable<ModelPart> headParts() {
      return ImmutableList.of();
   }

   protected Iterable<ModelPart> bodyParts() {
      return ImmutableList.of(this.leftWing, this.rightWing);
   }

   public void setupAnim(T entity, float p_102545_, float p_102546_, float ageInTicks, float p_102548_, float p_102549_) {
//      float f = 0.2617994F;
//      float f1 = -0.2617994F;
//      float f2 = 0.0F;
//      float f3 = 0.0F;
//      if (entity.isFallFlying()) {
//         float f4 = 1.0F;
//         Vec3 vec3 = entity.getDeltaMovement();
//         if (vec3.y < 0.0D) {
//            Vec3 vec31 = vec3.normalize();
//            f4 = 1.0F - (float)Math.pow(-vec31.y, 1.5D);
//         }
//
//         f = f4 * 0.34906584F + (1.0F - f4) * f;
//         f1 = f4 * (-(float)Math.PI / 2F) + (1.0F - f4) * f1;
//      } else if (entity.isCrouching()) {
//         f = 0.6981317F;
//         f1 = (-(float)Math.PI / 4F);
//         f2 = 3.0F;
//         f3 = 0.08726646F;
//      }
//
//      this.leftWing.y = f2;
//      if (entity instanceof AbstractClientPlayer) {
//         AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)entity;
//         abstractclientplayer.elytraRotX = (float)((double)abstractclientplayer.elytraRotX + (double)(f - abstractclientplayer.elytraRotX) * 0.1D);
//         abstractclientplayer.elytraRotY = (float)((double)abstractclientplayer.elytraRotY + (double)(f3 - abstractclientplayer.elytraRotY) * 0.1D);
//         abstractclientplayer.elytraRotZ = (float)((double)abstractclientplayer.elytraRotZ + (double)(f1 - abstractclientplayer.elytraRotZ) * 0.1D);
//         this.leftWing.xRot = abstractclientplayer.elytraRotX;
//         this.leftWing.yRot = abstractclientplayer.elytraRotY;
//         this.leftWing.zRot = abstractclientplayer.elytraRotZ;
//      } else {
//         this.leftWing.xRot = f;
//         this.leftWing.zRot = f1;
//         this.leftWing.yRot = f3;
//      }
//
//      this.rightWing.yRot = -this.leftWing.yRot;
//      this.rightWing.y = this.leftWing.y;
//      this.rightWing.xRot = this.leftWing.xRot;
//      this.rightWing.zRot = -this.leftWing.zRot;

//      if (entity.isCrouching()) {
//         this.body.xRot = 0.5F;
//         this.body.y = 3.2F;
//      } else {
//         this.body.xRot = 0.0F;
//         this.body.y = 0.0F;
//      }

//      float base = -2.5307F;
//      this.rightWing.yRot = (float) (base + Math.sin(ageInTicks) * 0.2F);
//      this.leftWing.yRot = -this.rightWing.yRot;
   }
}