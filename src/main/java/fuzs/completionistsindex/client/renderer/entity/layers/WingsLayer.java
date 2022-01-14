package fuzs.completionistsindex.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import fuzs.completionistsindex.client.model.WingsModel;
import fuzs.completionistsindex.client.registry.ModClientRegistry;
import fuzs.completionistsindex.world.item.WingsItem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public class WingsLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
   private final WingsModel<T> wingsModel;

   public WingsLayer(RenderLayerParent<T, M> p_174493_, EntityModelSet p_174494_) {
      super(p_174493_);
      this.wingsModel = new WingsModel<>(p_174494_.bakeLayer(ModClientRegistry.WINGS));
   }

   @Override
   public void render(PoseStack poseStack, MultiBufferSource p_116952_, int p_116953_, T p_116954_, float p_116955_, float p_116956_, float p_116957_, float p_116958_, float p_116959_, float p_116960_) {
      ItemStack itemstack = p_116954_.getItemBySlot(EquipmentSlot.OFFHAND);
      if (itemstack.getItem() instanceof WingsItem wingsItem) {
         if (!wingsItem.getWingsType().canRenderWings()) return;
         ItemStack itemstack2 = p_116954_.getItemBySlot(EquipmentSlot.CHEST);
         if (!itemstack2.isEmpty() && !(itemstack2.getItem() instanceof ArmorItem)) return;
         poseStack.pushPose();
         if (itemstack2.getItem() instanceof ArmorItem) {
            poseStack.translate(0.0, 0.0, 0.065);
         }
         if (this.getParentModel() instanceof HumanoidModel model) {
            poseStack.translate(model.body.x / 16.0, model.body.y / 16.0, model.body.z / 16.0);
            if (model.body.zRot != 0.0)
               poseStack.mulPose(Vector3f.ZP.rotation(model.body.zRot));
            if (model.body.yRot != 0.0)
               poseStack.mulPose(Vector3f.YP.rotation(model.body.yRot));
            if (model.body.xRot != 0.0)
               poseStack.mulPose(Vector3f.XP.rotation(model.body.xRot));
         }
         this.getParentModel().copyPropertiesTo(this.wingsModel);
         this.wingsModel.setupAnim(p_116954_, p_116955_, p_116956_, p_116958_, p_116959_, p_116960_);
         VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(p_116952_, RenderType.armorCutoutNoCull(wingsItem.getWingsType().getTextureLocation()), false, itemstack.hasFoil());
         this.wingsModel.renderToBuffer(poseStack, vertexconsumer, p_116953_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
         poseStack.popPose();
      }
   }
}
