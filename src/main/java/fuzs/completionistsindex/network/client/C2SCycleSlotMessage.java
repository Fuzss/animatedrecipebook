package fuzs.completionistsindex.network.client;

import fuzs.completionistsindex.util.SlotUtil;
import fuzs.puzzleslib.network.message.Message;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class C2SCycleSlotMessage implements Message {
    private boolean cycleRight;

    public C2SCycleSlotMessage() {
    }

    public C2SCycleSlotMessage(boolean cycleRight) {
        this.cycleRight = cycleRight;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.cycleRight);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.cycleRight = buf.readBoolean();
    }

    @Override
    public CycleSlotHandler makeHandler() {
        return new CycleSlotHandler();
    }

    private static class CycleSlotHandler extends PacketHandler<C2SCycleSlotMessage> {
        @Override
        public void handle(C2SCycleSlotMessage packet, Player player, Object gameInstance) {
            if (!player.isSpectator()) {
                if (packet.cycleRight) {
                    final int rightSlot = SlotUtil.getRightSlot(player);
                    if (rightSlot != -1) {
                        this.swapSlotWithSelected(player, rightSlot);
                    }
                } else {
                    final int leftSlot = SlotUtil.getLeftSlot(player);
                    if (leftSlot != -1) {
                        this.swapSlotWithSelected(player, leftSlot);
                    }
                }
            }
        }

        private void swapSlotWithSelected(Player player, int slot) {
            final Inventory inventory = player.getInventory();
            ItemStack itemstack = inventory.items.get(inventory.selected);
            inventory.items.set(inventory.selected, inventory.items.get(slot));
            if (!itemstack.isEmpty()) {
                inventory.items.set(slot, itemstack);
                player.stopUsingItem();
            }
        }
    }
}
