package fuzs.completionistsindex.client.handler;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.config.ClientConfig;
import fuzs.completionistsindex.mixin.client.accessor.MouseHandlerAccessor;
import fuzs.completionistsindex.util.SlotUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MouseScrollHandler {
    private final Minecraft minecraft;
    private final MouseHandler mouseHandler;

    public MouseScrollHandler(Minecraft minecraft) {
        this.minecraft = minecraft;
        this.mouseHandler = minecraft.mouseHandler;
    }

    @SubscribeEvent
    public void onMouseScroll(final InputEvent.MouseScrollEvent evt) {
        final Player player = this.minecraft.player;
        if (!player.isSpectator()) {
            if (isKeyDown(CompletionistsIndex.CONFIG.client().scrollingModifierKey)) {
                double totalScroll = evt.getScrollDelta() + ((MouseHandlerAccessor) this.mouseHandler).getAccumulatedScroll();
                if (totalScroll > 0.0) {
                    if (SlotUtil.cycleSlotsLeft(player, KeyBindingHandler::swapSlots)) {
                        KeyBindingHandler.setPopTimeColumn(player);
                    }
                } else if (totalScroll < 0.0) {
                    if (SlotUtil.cycleSlotsRight(player, KeyBindingHandler::swapSlots)) {
                        KeyBindingHandler.setPopTimeColumn(player);
                    }
                }
                evt.setCanceled(true);
            }
        }
    }

    public static boolean isKeyDown(ClientConfig.ModifierKey modifierKey) {
        return switch (modifierKey) {
            case NONE -> true;
            case CONTROL -> Screen.hasControlDown();
            case SHIFT -> Screen.hasShiftDown();
            case ALT -> Screen.hasAltDown();
        };
    }
}
