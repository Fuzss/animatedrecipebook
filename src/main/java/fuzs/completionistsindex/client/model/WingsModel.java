package fuzs.completionistsindex.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;

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
      partdefinition.addOrReplaceChild("left_wing",  CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-16.0F, -14.0F, 0.0F, 16.0F, 16.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 12.0F, 1.0F, 0.0F, 2.5307F, 0.0F));
      partdefinition.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -10.0F, 0.0F, 16.0F, 16.0F, 0.0F), PartPose.offsetAndRotation(0.0F, 8.0F, 1.0F, 0.0F, -2.5307F, 0.0F));
      return LayerDefinition.create(meshdefinition, 32, 16);
   }

   @Override
   protected Iterable<ModelPart> headParts() {
      return ImmutableList.of();
   }

   @Override
   protected Iterable<ModelPart> bodyParts() {
      return ImmutableList.of(this.leftWing, this.rightWing);
   }

   @Override
   public void setupAnim(T entity, float p_102545_, float p_102546_, float ageInTicks, float p_102548_, float p_102549_) {
      float movementRange = 0.0436F;
      float multiplier = 1.0F;
      if (entity instanceof AbstractClientPlayer player) {
         if (player.getAbilities().flying) {
            movementRange *= 7.5F;
         } else {
            multiplier = 0.35F;
         }
      }
      this.leftWing.yRot = 2.5307F + (float) Math.sin(ageInTicks * multiplier) * movementRange;
      this.rightWing.yRot = -this.leftWing.yRot;
   }
}