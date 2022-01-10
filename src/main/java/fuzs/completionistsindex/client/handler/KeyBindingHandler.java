package fuzs.completionistsindex.client.handler;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.network.client.C2SCycleSlotMessage;
import fuzs.completionistsindex.util.SlotUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyBindingHandler {
    public final KeyMapping keyCycleLeft = new KeyMapping("key.cycleLeft", 71, "key.categories.inventory");
    public final KeyMapping keyCycleRight = new KeyMapping("key.cycleRight", 72, "key.categories.inventory");

    public void init() {
        ClientRegistry.registerKeyBinding(this.keyCycleLeft);
        ClientRegistry.registerKeyBinding(this.keyCycleRight);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent evt) {
        if (evt.phase != TickEvent.Phase.START) return;
        final Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.getOverlay() == null && (minecraft.screen == null || minecraft.screen.passEvents)) {
            minecraft.getProfiler().push("Keybindings");
            this.handleKeybinds(minecraft.player);
        }
    }

    private void handleKeybinds(Player player) {
        while (this.keyCycleLeft.consumeClick()) {
            if (!player.isSpectator() && SlotUtil.isLeftSlotValid(player)) {
                CompletionistsIndex.NETWORK.sendToServer(new C2SCycleSlotMessage(false));
            }
        }
        while (this.keyCycleRight.consumeClick()) {
            if (!player.isSpectator() && SlotUtil.isRightSlotValid(player)) {
                CompletionistsIndex.NETWORK.sendToServer(new C2SCycleSlotMessage(true));
            }
        }
    }
}
