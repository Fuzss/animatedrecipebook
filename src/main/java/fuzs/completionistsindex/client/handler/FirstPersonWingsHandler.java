package fuzs.completionistsindex.client.handler;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.world.item.WingsItem;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FirstPersonWingsHandler {
    @SubscribeEvent
    public void onRenderHand(final RenderHandEvent evt) {
        if (evt.getHand() == InteractionHand.OFF_HAND && evt.getItemStack().getItem() instanceof WingsItem) {
            if (!CompletionistsIndex.CONFIG.client().renderOffhandRing) evt.setCanceled(true);
        }
    }
}
