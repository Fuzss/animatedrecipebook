package fuzs.completionistsindex.util;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SlotUtil {
    public static int cycleHotbarSlotLeft(Player player) {
        return cycleSlotLeft(player, player.getInventory().selected);
    }

    public static int cycleSlotLeft(Player player, int slot) {
        int rightSlot = -1;
        final Inventory inventory = player.getInventory();
        for (int i = 3; i > 0; i--) {
            int slotIndex = (i * 9 + slot) % 36;
            if (Inventory.isHotbarSlot(slotIndex) || !inventory.items.get(slotIndex).isEmpty()) {
                rightSlot = slotIndex;
                break;
            }
        }
        return rightSlot;
    }

    public static int cycleHotbarSlotRight(Player player) {
        return cycleSlotRight(player, player.getInventory().selected);
    }

    public static int cycleSlotRight(Player player, int slot) {
        int leftSlot = -1;
        final Inventory inventory = player.getInventory();
        for (int i = 1; i < 4; i++) {
            int slotIndex = (i * 9 + slot) % 36;
            if (Inventory.isHotbarSlot(slotIndex) || !inventory.items.get(slotIndex).isEmpty()) {
                leftSlot = slotIndex;
                break;
            }
        }
        return leftSlot;
    }

    public static void cycleSlotsRight(Player player) {
        cycleSlotsRight(player, SlotUtil::swapSlots);
    }

    public static void cycleSlotsRight(Player player, SlotSwapper slotSwapper) {
        int currentSlot = player.getInventory().selected + 9;
        while (!Inventory.isHotbarSlot(currentSlot)) {
            int nextSlot = SlotUtil.cycleSlotRight(player, currentSlot);
            slotSwapper.swapSlots(player, currentSlot, nextSlot);
            currentSlot = nextSlot;
        }
    }

    public static void cycleSlotsLeft(Player player) {
        cycleSlotsLeft(player, SlotUtil::swapSlots);
    }

    public static void cycleSlotsLeft(Player player, SlotSwapper slotSwapper) {
        int currentSlot = player.getInventory().selected + 27;
        while (!Inventory.isHotbarSlot(currentSlot)) {
            int nextSlot = SlotUtil.cycleSlotLeft(player, currentSlot);
            slotSwapper.swapSlots(player, currentSlot, nextSlot);
            currentSlot = nextSlot;
        }
    }

    private static void swapSlots(Player player, int currentSlot, int nextSlot) {
        final NonNullList<ItemStack> items = player.getInventory().items;
        ItemStack itemstack = items.get(nextSlot);
        items.set(nextSlot, items.get(currentSlot));
        items.set(currentSlot, itemstack);
    }

    public interface SlotSwapper {
        void swapSlots(Player player, int currentSlot, int nextSlot);
    }
}
