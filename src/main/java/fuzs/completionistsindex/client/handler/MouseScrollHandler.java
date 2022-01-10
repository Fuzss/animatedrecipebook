package fuzs.completionistsindex.client.handler;

import fuzs.completionistsindex.CompletionistsIndex;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public class MouseScrollHandler {
    private final Minecraft minecraft = Minecraft.getInstance();
    private double accumulatedScroll;

    @SubscribeEvent
    public void onMouseScroll(final InputEvent.MouseScrollEvent evt) {
        if (this.accumulatedScroll != 0.0D && Math.signum(evt.getScrollDelta()) != Math.signum(this.accumulatedScroll)) {
            this.accumulatedScroll = 0.0D;
        }
        this.accumulatedScroll += evt.getScrollDelta();
        float f1 = (float)((int)this.accumulatedScroll);
        if (f1 == 0.0F) return;
        this.accumulatedScroll -= f1;
        if (!this.minecraft.player.isSpectator()) {
            if (CompletionistsIndex.CONFIG.client().scrollingModifierKey.isKeyDown()) {

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
