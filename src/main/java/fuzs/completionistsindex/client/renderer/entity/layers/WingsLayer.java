package fuzs.completionistsindex.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.client.model.WingsModel;
import fuzs.completionistsindex.client.registry.ModClientRegistry;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WingsLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
   private static final ResourceLocation WINGS_LOCATION = new ResourceLocation(CompletionistsIndex.MOD_ID, "textures/models/wings/feather.png");
   private final WingsModel<T> elytraModel;

   public WingsLayer(RenderLayerParent<T, M> p_174493_, EntityModelSet p_174494_) {
      super(p_174493_);
      this.elytraModel = new WingsModel<>(p_174494_.bakeLayer(ModClientRegistry.WINGS));
   }

   public void render(PoseStack poseStack, MultiBufferSource p_116952_, int p_116953_, T p_116954_, float p_116955_, float p_116956_, float p_116957_, float p_116958_, float p_116959_, float p_116960_) {
      ItemStack itemstack = p_116954_.getItemBySlot(EquipmentSlot.CHEST);
      if (this.shouldRender(itemstack, p_116954_)) {
         if (!itemstack.isEmpty() && !(itemstack.getItem() instanceof ArmorItem)) return;
         poseStack.pushPose();
         if (itemstack.getItem() instanceof ArmorItem)
         poseStack.translate(0.0, 0.0, 0.065);
         if (this.getParentModel() instanceof HumanoidModel) {
            final ModelPart body = ((HumanoidModel<T>) this.getParentModel()).body;
            poseStack.translate(body.x / 16.0, body.y / 16.0, body.z / 16.0);
            if (body.zRot != 0.0)
               poseStack.mulPose(Vector3f.ZP.rotation(body.zRot));
            if (body.yRot != 0.0)
               poseStack.mulPose(Vector3f.YP.rotation(body.yRot));
            if (body.xRot != 0.0)
               poseStack.mulPose(Vector3f.XP.rotation(body.xRot));
         }
         this.getParentModel().copyPropertiesTo(this.elytraModel);
         this.elytraModel.setupAnim(p_116954_, p_116955_, p_116956_, p_116958_, p_116959_, p_116960_);
         VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(p_116952_, RenderType.armorCutoutNoCull(WINGS_LOCATION), false, itemstack.hasFoil());
         this.elytraModel.renderToBuffer(poseStack, vertexconsumer, p_116953_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
         poseStack.popPose();
      }
   }

   /**
    * Determines if the ElytraLayer should render.
    * ItemStack and Entity are provided for modder convenience,
    * For example, using the same ElytraLayer for multiple custom Elytra.
    *
    * @param stack  The Elytra ItemStack
    * @param entity The entity being rendered.
    * @return If the ElytraLayer should render.
    */
   public boolean shouldRender(ItemStack stack, T entity) {
      return true;
   }

}
