package fuzs.completionistsindex.client.handler;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.network.client.C2SCycleSlotsMessage;
import fuzs.completionistsindex.util.SlotUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
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
            this.handleKeybinds(minecraft.player);
        }
    }

    private void handleKeybinds(Player player) {
        while (this.keyCycleLeft.consumeClick()) {
            if (!player.isSpectator() && SlotUtil.cycleHotbarSlotLeft(player) != -1) {
                SlotUtil.cycleSlotsLeft(player);
                CompletionistsIndex.NETWORK.sendToServer(new C2SCycleSlotsMessage(false));
//                this.cycleSlots(player, false);
                this.setPopTimeColumn(player);
            }
        }
        while (this.keyCycleRight.consumeClick()) {
            if (!player.isSpectator() && SlotUtil.cycleHotbarSlotRight(player) != -1) {
                SlotUtil.cycleSlotsRight(player);
                CompletionistsIndex.NETWORK.sendToServer(new C2SCycleSlotsMessage(true));
//                this.cycleSlots(player, true);
                this.setPopTimeColumn(player);
            }
        }
    }

    private void setPopTimeColumn(Player player) {
        final int selected = player.getInventory().selected;
        for (int i = 0; i < 4; i++) {
            final ItemStack itemStack = player.getInventory().items.get(selected + 9 * i);
            if (!itemStack.isEmpty()) itemStack.setPopTime(5);
        }
    }

    private void cycleSlots(Player player, boolean cycleRight) {
        if (!player.isSpectator()) {
            final Inventory inventory = player.getInventory();
            final int selected = inventory.selected;
            if (cycleRight) {
                if (SlotUtil.cycleHotbarSlotRight(player) != -1) {
                    int currentSlot = selected + 9;
                    while (selected != currentSlot) {
                        int nextSlot = SlotUtil.cycleSlotRight(player, currentSlot);
                        this.swapSlots(player, currentSlot, nextSlot);
                        currentSlot = nextSlot;
                    }
                    player.stopUsingItem();
                }
            } else {
                if (SlotUtil.cycleHotbarSlotLeft(player) != -1) {
                    int currentSlot = selected + 27;
                    while (selected != currentSlot) {
                        int nextSlot = SlotUtil.cycleSlotLeft(player, currentSlot);
                        this.swapSlots(player, currentSlot, nextSlot);
                        currentSlot = nextSlot;
                    }
                    player.stopUsingItem();
                }
            }
        }
    }

    private void swapSlots(Player player, int currentSlot, int nextSlot) {
        final Minecraft minecraft = Minecraft.getInstance();
        minecraft.gameMode.handleInventoryMouseClick(player.containerMenu.containerId, currentSlot, nextSlot, ClickType.SWAP, player);
    }
}
