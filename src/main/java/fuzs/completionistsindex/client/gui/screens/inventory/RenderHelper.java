package fuzs.completionistsindex.client.gui.screens.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

// petra this is the 5th time you've copied RenderHelper from Vazkii this week
public class RenderHelper {

    public static void renderItemStackInGui(PoseStack poseStack, ItemStack stack, int posX, int posY) {
        transferMsToGl(poseStack, () -> Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(stack, posX, posY));
    }

    /**
     * Temporary shim to allow methods such as
     * {@link net.minecraft.client.renderer.entity.ItemRenderer#renderAndDecorateItem}
     * to support matrixstack transformations. Hopefully Mojang finishes this migration up...
     * Transfers the current CPU matrixstack to the openGL matrix stack, then runs the provided function
     * Assumption: the "root" state of the MatrixStack is same as the currently GL state,
     * such that multiplying the MatrixStack to the current GL matrix state will get us where we want to be.
     * If there have been intervening changes to the GL matrix state since the MatrixStack was constructed, then this
     * won't work.
     */
    public static void transferMsToGl(PoseStack poseStack, Runnable toRun) {
        PoseStack mvs = RenderSystem.getModelViewStack();
        mvs.pushPose();
        mvs.mulPoseMatrix(poseStack.last().pose());
        RenderSystem.applyModelViewMatrix();
        toRun.run();
        mvs.popPose();
        RenderSystem.applyModelViewMatrix();
    }
}
