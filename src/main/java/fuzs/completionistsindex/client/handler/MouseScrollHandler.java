package fuzs.completionistsindex.client.handler;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.util.SlotUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MouseScrollHandler {
    private final Minecraft minecraft = Minecraft.getInstance();
    private double accumulatedScroll;

    @SubscribeEvent
    public void onMouseScroll(final InputEvent.MouseScrollEvent evt) {
        if (this.accumulatedScroll != 0.0 && Math.signum(evt.getScrollDelta()) != Math.signum(this.accumulatedScroll)) {
            this.accumulatedScroll = 0.0;
        }
        this.accumulatedScroll += evt.getScrollDelta();
        float f1 = (float)((int)this.accumulatedScroll);
        if (f1 == 0.0F) return;
        this.accumulatedScroll -= f1;
        final Player player = this.minecraft.player;
        if (!player.isSpectator()) {
            CompletionistsIndex.LOGGER.info("cycling");
            if (CompletionistsIndex.CONFIG.client().scrollingModifierKey.isKeyDown()) {
                CompletionistsIndex.LOGGER.info("key is down");
                final float signum = Math.signum(f1);
                if (signum == 1.0F) {
                    if (!player.isSpectator() && SlotUtil.cycleHotbarSlotRight(player) != -1) {
                        SlotUtil.cycleSlotsRight(player, KeyBindingHandler::swapSlots);
                        KeyBindingHandler.setPopTimeColumn(player);
                    }
                } else if (signum == -1.0F) {
                    if (!player.isSpectator() && SlotUtil.cycleHotbarSlotLeft(player) != -1) {
                        SlotUtil.cycleSlotsLeft(player, KeyBindingHandler::swapSlots);
                        KeyBindingHandler.setPopTimeColumn(player);
                    }
                }
                evt.setCanceled(true);
            }
        }
    }

    public enum ModifierKey {
        NONE, CONTROL, SHIFT, ALT;

        public boolean isKeyDown() {
            return switch (this) {
                case NONE -> true;
                case CONTROL -> Screen.hasControlDown();
                case SHIFT -> Screen.hasShiftDown();
                case ALT -> Screen.hasAltDown();
            };
        }
    }
}
