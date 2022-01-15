package fuzs.completionistsindex.client.handler;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.world.item.WingsItem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FirstPersonWingsHandler {
    @SubscribeEvent
    public void onRenderHand(final RenderHandEvent evt) {
        if (evt.getHand() == InteractionHand.OFF_HAND && evt.getItemStack().getItem() instanceof WingsItem) {
            if (!CompletionistsIndex.CONFIG.client().renderOffhandRing) evt.setCanceled(true);
        }
    }

    public static void init() {
        OverlayRegistry.registerOverlayTop(new ResourceLocation(CompletionistsIndex.MOD_ID, "flight_endurance").toString(), (gui, mStack, partialTicks, screenWidth, screenHeight) -> {
            final Minecraft minecraft = Minecraft.getInstance();
            if (!minecraft.options.hideGui && gui.shouldDrawSurvivalElements()) {
                gui.setupOverlayRenderState(true, false);
                gui.renderHealth(screenWidth, screenHeight, mStack);
            }
        });
    }
}
