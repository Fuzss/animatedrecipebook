package fuzs.completionistsindex.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Vector3f;
import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.client.registry.ModClientRegistry;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TeleportChargeEffectLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
   public static final ResourceLocation TELEPORT_LOCATION = new ResourceLocation(CompletionistsIndex.MOD_ID, "textures/entity/teleport_ray.png");
   public static final ResourceLocation TELEPORT_DEEP_LOCATION = new ResourceLocation(CompletionistsIndex.MOD_ID, "textures/entity/teleport_ray_deep.png");
   public static final String BOX = "box";
   private final ModelPart teleportBox;
   private final ModelPart teleportDeepBox;

   public TeleportChargeEffectLayer(RenderLayerParent<T, M> p_174540_, EntityModelSet p_174541_) {
      super(p_174540_);
      ModelPart modelpart = p_174541_.bakeLayer(ModClientRegistry.TELEPORT_RAY);
      this.teleportBox = modelpart.getChild("teleport_box");
      this.teleportDeepBox = modelpart.getChild("teleport_deep_box");
   }

   public static LayerDefinition createLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();
      partdefinition.addOrReplaceChild("teleport_box", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 32.0F, 16.0F), PartPose.ZERO);
      partdefinition.addOrReplaceChild("teleport_deep_box", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 32.0F, 16.0F), PartPose.ZERO);
      return LayerDefinition.create(meshdefinition, 32, 48);
   }

   public void render(PoseStack p_117526_, MultiBufferSource p_117527_, int p_117528_, T p_117529_, float p_117530_, float p_117531_, float p_117532_, float p_117533_, float p_117534_, float p_117535_) {
      if (p_117529_.isUsingItem() && p_117529_.getUseItem().getItem() instanceof BowItem) {
         float f = (float)p_117529_.tickCount + p_117533_;
         VertexConsumer vertexconsumer = p_117527_.getBuffer(RenderType.energySwirl(TELEPORT_LOCATION, f * 0.01F % 1.0F, f * 0.01F % 1.0F));

         for(int i = -1; i <= 1; ++i) {
            p_117526_.pushPose();
//            float f3 = p_117533_ * (float)(-(45 + i * 5)) * 0.05F;
//            p_117526_.mulPose(Vector3f.YP.rotationDegrees(f3));
            float f1 = 1.5F + 0.05F * (float)i;
            p_117526_.scale(f1, f1, f1);
            p_117526_.translate(0.0D, 1.0, 0.0D);
            this.teleportBox.render(p_117526_, vertexconsumer, p_117528_, OverlayTexture.NO_OVERLAY);
            p_117526_.popPose();
         }

      }
   }
}